package netty.smpp.api.message.tlv;

/**
 * Value encoder for string types. Operates on the java.lang.String type.
 *
 * @author Oran Kelly
 * @version $Id: StringEncoder.java 255 2006-03-09 09:34:37Z orank $
 */
public class StringEncoder implements netty.smpp.api.message.tlv.Encoder {

    private static final String ASCII_UNSUPPORTED_MSG = "Your JVM doesn't support ASCII!";
    private static final String ASCII = "US-ASCII";

    /**
     * Create a new StringEncoder.
     */
    public StringEncoder() {
    }

    public void writeTo(netty.smpp.api.message.tlv.Tag tag, Object value, byte[] b, int offset) {
        try {
            String s = value.toString();
            int len = s.length();

            byte[] b1 = s.getBytes(ASCII);
            System.arraycopy(b1, 0, b, offset, len);
            b[offset + len] = (byte) 0;
        } catch (java.io.UnsupportedEncodingException x) {
            // Java spec _requires_ US-ASCII support
            throw new RuntimeException(ASCII_UNSUPPORTED_MSG);
        }
    }

    public Object readFrom(netty.smpp.api.message.tlv.Tag tag, byte[] b, int offset, int length) {
        try {
            String s = new String(b, offset, length - 1, ASCII);
            return s;
        } catch (java.io.UnsupportedEncodingException x) {
            // Java spec _requires_ US-ASCII support
            throw new RuntimeException(ASCII_UNSUPPORTED_MSG);
        }
    }

    public int getValueLength(Tag tag, Object value) {
        // 1 for the nul byte
        return value.toString().length() + 1;
    }
}

