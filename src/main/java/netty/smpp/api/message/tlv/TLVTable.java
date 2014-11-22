package netty.smpp.api.message.tlv;

import netty.smpp.api.util.SMPPIO;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Table of optional parameters (TLVs).
 * <p>
 * TLV stands for Tag/Length/Value and was a capability added to SMPP version
 * 3.4. It is an extensible means of adding new parameter types to SMPP packets.
 * Each optional parameter has a 2-byte tag, which is a unique identifier of
 * that parameter, a 2-byte length, which is an integer value representing the
 * length of the value of the parameter and a value. The value may be of various
 * types including integers, C Strings, octet strings, bit masks etc. The tag
 * defines the type of the value.
 * </p>
 * <p>
 * This class holds a mapping of tags to values. Each SMPP packet holds a TLV
 * table which holds that packet's set of current optional parameters. Upon
 * serializing the packet to an output stream or byte array, the format of the
 * serialized packet is:
 * </p>
 * <p/>
 * <pre>
 *
 *    +-------------------------+
 *    | SMPP Packet             |
 *    | +----------------------+|
 *    | | SMPP Header          ||
 *    | +----------------------+|
 *    | |                      ||
 *    | |                      ||
 *    | | Mandatory parameters ||
 *    | |                      ||
 *    | |                      ||
 *    | +----------------------+|
 *    | | Optional parameters  ||
 *    | | +------------------+ ||
 *    | | | Tag/Length/Value | ||
 *    | | +------------------+ ||
 *    | | |     ...          | ||
 *    | | +------------------+ ||
 *    | +----------------------+|
 *    +-------------------------+
 *
 * </pre>
 *
 * @author Oran Kelly
 * @version $Id: TLVTable.java 466 2009-06-16 12:26:57Z orank $
 */
public class TLVTable implements java.io.Serializable {
    static final long serialVersionUID = -4113000096792513355L;
    /**
     * Map of tag to values.
     */
    private Map map = new HashMap();

    /**
     * Undecoded options. TLVTable is lazy about decoding optional parameters.
     * It will decode a TLV param only when it is requested using the
     * <code>get</code> method. Certain method calls, however, will force
     * TLVTable to parse the entire set of options (using
     * <code>parseAllOpts</code>). When that happens, this array is released
     * for garbage collection.
     */
    private byte[] opts;

    /**
     * Create a new, empty, TLVTable.
     */
    public TLVTable() {
    }

    /**
     * Decode a full set of optional parameters from a byte array.
     *
     * @param b      The byte array to decode from.
     * @param offset The first byte of the tag of the first optional parameter.
     * @param len    The length in the byte array of all the optional parameters.
     */
    public void readFrom(byte[] b, int offset, int len) {
        synchronized (map) {
            opts = new byte[len];
            System.arraycopy(b, offset, opts, 0, len);
        }
    }

    /**
     * Encode all the optional parameters in this table to an output stream.
     *
     * @param out The output stream to encode the parameters to.
     * @throws java.io.IOException If an error occurs writing to the output stream.
     */
    public void writeTo(OutputStream out) throws IOException {
        synchronized (map) {
            byte[] buffer = new byte[1024];

            Iterator i = map.keySet().iterator();
            while (i.hasNext()) {
                netty.smpp.api.message.tlv.Tag t = (netty.smpp.api.message.tlv.Tag) i.next();
                netty.smpp.api.message.tlv.Encoder enc = t.getEncoder();
                Object v = map.get(t);
                int l = enc.getValueLength(t, v);

                if (buffer.length < (l + 4)) {
                    buffer = new byte[l + 4];
                }

                SMPPIO.intToBytes(t.intValue(), 2, buffer, 0);
                SMPPIO.intToBytes(l, 2, buffer, 2);
                enc.writeTo(t, v, buffer, 4);

                // write the buffer out.
                out.write(buffer, 0, l + 4);
            }
        }
    }

    /**
     * Get the value for a tag. Note that this method can return null in two
     * cases: if the parameter is not set or if the value is null, which can
     * occur if the particular tag type has no value. To check if a parameter
     * which has no value is set, use {@link #isSet}.
     *
     * @param tag The tag to get the value for.
     * @return The currently set value for <code>tag</code>, or null if it is
     * not set.
     */
    public Object get(netty.smpp.api.message.tlv.Tag tag) {
        Object v = map.get(tag);

        if (v == null) {
            v = getValueFromBytes(tag);
        }

        return v;
    }

    /**
     * Get the value for a tag.
     *
     * @see #get(netty.smpp.api.message.tlv.Tag)
     */
    public Object get(int tag) {
        netty.smpp.api.message.tlv.Tag tagObj = netty.smpp.api.message.tlv.Tag.getTag(tag);
        Object v = map.get(tagObj);

        if (v == null) {
            v = getValueFromBytes(tagObj);
        }

        return v;
    }

    /**
     * Check if an optional parameter currently has a value set.
     *
     * @param tag The tag of the parameter to check is set.
     * @return true if the parameter is set, false if not.
     */
    public boolean isSet(netty.smpp.api.message.tlv.Tag tag) {
        if (opts != null) {
            parseAllOpts();
        }
        return map.containsKey(tag);
    }

    /**
     * Set a value for an optional parameter.
     *
     * @param tag   The tag of the parameter to set.
     * @param value The value of the parameter to set.
     * @return The previous value for the parameter, or null if there was none.
     * @throws netty.smpp.api.message.tlv.BadValueTypeException        if an attempt is made to set a value using a Java type that
     *                                                                 is not allowed for that parameter type.
     * @throws netty.smpp.api.message.tlv.InvalidSizeForValueException if the value's encoded length is outside the bounds allowed
     *                                                                 for that parameter.
     */
    public Object set(netty.smpp.api.message.tlv.Tag tag, Object value) throws BadValueTypeException,
            InvalidSizeForValueException {
        synchronized (map) {
            if (opts != null) {
                parseAllOpts();
            }

            if (tag.getType() == null) {
                if (value != null) {
                    throw new BadValueTypeException("Tag "
                            + Integer.toHexString(tag.intValue())
                            + " does not accept a value.");
                }
            } else if (!tag.getType().isAssignableFrom(value.getClass())) {
                throw new BadValueTypeException("Tag "
                        + Integer.toHexString(tag.intValue())
                        + " expects a value of type " + tag.getType());
            }

            // Enforce the length restrictions on the Value specified by the
            // Tag.
            int min = tag.getMinLength();
            int max = tag.getMaxLength();
            int actual = tag.getEncoder().getValueLength(tag, value);

            boolean illegal = min > -1 && actual < min;
            if (!illegal) {
                illegal = max > -1 && actual > max;
            }

            if (illegal) {
                throw new InvalidSizeForValueException("Tag "
                        + Integer.toHexString(tag.intValue())
                        + " must have a length in the range " + min
                        + " <= len <= " + max);
            }

            return map.put(tag, value);
        }
    }

    /**
     * Clear all optional parameters out of this table.
     */
    public void clear() {
        synchronized (map) {
            map.clear();
        }
    }

    /**
     * Force the TLVTable to parse all the optional parameters from the internal
     * byte array and place them in the map. Normally, TLVTable is lazy about
     * parsing parameters. It will only decode them and place them in the
     * internal map when they are requested using {@link #get}. Calling this
     * method causes all the parameters to be parsed and placed in the internal
     * map and the byte array containing the parameter's bytes to be released
     * for garbage collection.
     * <p/>
     * It is not normally needed for an application to call this method.
     * <code>TLVTable</code> uses it internally when necessary to ensure there
     * is no loss of synchronization between the internal map and the byte
     * array.
     */
    public final void parseAllOpts() {
        synchronized (map) {
            int p = 0;

            while (p < opts.length) {
                Object val = null;
                netty.smpp.api.message.tlv.Tag t = netty.smpp.api.message.tlv.Tag.getTag(SMPPIO.bytesToInt(opts, p, 2));
                netty.smpp.api.message.tlv.Encoder enc = t.getEncoder();
                int l = SMPPIO.bytesToInt(opts, p + 2, 2);

                val = enc.readFrom(t, opts, p + 4, l);
                map.put(t, val);

                p += 4 + l;
            }

            opts = null;
        }
    }

    /**
     * Get the value of an option from the <code>opts</code> byte array.
     *
     * @param tag The tag to get the value for.
     * @return The value object for tag <code>tag</code>.<code>null</code>
     * if it is not set.
     */
    private Object getValueFromBytes(netty.smpp.api.message.tlv.Tag tag) {
        if (opts == null || opts.length < 4) {
            return null;
        }

        netty.smpp.api.message.tlv.Encoder enc = tag.getEncoder();
        Object val = null;
        int p = 0;
        while (true) {
            int t = SMPPIO.bytesToInt(opts, p, 2);
            int l = SMPPIO.bytesToInt(opts, p + 2, 2);

            if (tag.equals(t)) {
                val = enc.readFrom(tag, opts, p + 4, l);
                synchronized (map) {
                    map.put(tag, val);
                    break;
                }
            }

            p += 4 + l;
            if (p >= opts.length) {
                break;
            }
        }

        return val;
    }

    /**
     * Get the length the parameters in the table would encode as. The length of
     * an SMPP packet is determined by: <br>
     * <code>sizeof (smpp_header) + sizeof (mandatory_parameters)
     * + sizeof (optional_parameters).</code>
     * <br>
     * The value returned for this method is the last clause in this equation.
     *
     * @return The full length that the optional parameters would encode as.
     */
    public int getLength() {
        if (opts != null) {
            parseAllOpts();
        }

        // Length is going to be (number of options) * (2 bytes for tag) * (2
        // bytes for length) + (size of all encoded values)
        int length = map.size() * 4;
        netty.smpp.api.message.tlv.Tag tag;
        netty.smpp.api.message.tlv.Encoder enc;
        Iterator i = map.keySet().iterator();
        while (i.hasNext()) {
            tag = (Tag) i.next();
            enc = tag.getEncoder();
            length += enc.getValueLength(tag, map.get(tag));
        }

        return length;
    }

    /**
     * Get the set of tags in this TLVTable.
     *
     * @return A java.util.Set containing all the Tags in this TLVTable.
     */
    public java.util.Set tagSet() {
        if (opts != null) {
            parseAllOpts();
        }

        return map.keySet();
    }

    /**
     * Get a Collection view of the set of values in this TLVTable.
     *
     * @return A java.util.Collection view of all the values in this TLVTable.
     */
    public java.util.Collection values() {
        if (opts != null) {
            parseAllOpts();
        }

        return map.values();
    }
}

