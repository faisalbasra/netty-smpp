package netty.smpp.api.message.tlv;

import java.util.BitSet;

/**
 * Encoder for bit mask value types. This class encodes and decodes
 * {@link java.util.BitSet}objects to and from byte arrays.
 *
 * @version $Id: BitmaskEncoder.java 264 2006-03-09 14:00:40Z orank $
 * @see java.util.BitSet
 */
public class BitmaskEncoder implements netty.smpp.api.message.tlv.Encoder {

    /**
     * Create a new BitmaskEncoder.
     */
    public BitmaskEncoder() {
    }

    public void writeTo(netty.smpp.api.message.tlv.Tag tag, Object value, byte[] b, int offset) {
        try {
            BitSet bs = (BitSet) value;
            int l = tag.getLength();

            for (int i = 0; i < l; i++) {
                b[offset + i] = 0;

                for (int j = 0; j < 8; j++) {
                    if (bs.get((i * 8) + j)) {
                        b[offset + i] |= (byte) (1 << j);
                    }
                }
            }
        } catch (ClassCastException x) {
            throw new BadValueTypeException("Value must be of type "
                    + "java.util.BitSet");
        }
    }

    public Object readFrom(netty.smpp.api.message.tlv.Tag tag, byte[] b, int offset, int length) {
        BitSet bs = new BitSet();

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < 8; j++) {
                if ((b[offset + i] & (byte) (1 << j)) != 0) {
                    bs.set((i * 8) + j);
                }
            }
        }

        return bs;
    }

    public int getValueLength(Tag tag, Object value) {
        return tag.getLength();
    }
}
