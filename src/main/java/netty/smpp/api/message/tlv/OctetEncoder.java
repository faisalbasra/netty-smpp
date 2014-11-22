package netty.smpp.api.message.tlv;

/**
 * Encode an octet string to a byte array. This class is encoding byte arrays to
 * byte arrays! Therefore it's just copying bytes around. Not much more to it.
 *
 * @author Oran Kelly
 * @version $Id: OctetEncoder.java 255 2006-03-09 09:34:37Z orank $
 */
public class OctetEncoder implements netty.smpp.api.message.tlv.Encoder {

    private static final String BAD_VALUE_MSG = "Value must be of type byte[]";

    /**
     * Create a new OctetEncoder.
     */
    public OctetEncoder() {
    }

    public void writeTo(netty.smpp.api.message.tlv.Tag tag, Object value, byte[] b, int offset) {
        try {
            byte[] valBytes = (byte[]) value;
            System.arraycopy(valBytes, 0, b, offset, valBytes.length);
        } catch (ClassCastException x) {
            throw new BadValueTypeException(BAD_VALUE_MSG);
        }
    }

    public Object readFrom(netty.smpp.api.message.tlv.Tag tag, byte[] b, int offset, int length) {
        byte[] val = new byte[length];
        System.arraycopy(b, offset, val, 0, length);
        return val;
    }

    public int getValueLength(Tag tag, Object value) {
        try {
            byte[] b = (byte[]) value;
            return b.length;
        } catch (ClassCastException x) {
            throw new BadValueTypeException(BAD_VALUE_MSG);
        }
    }
}

