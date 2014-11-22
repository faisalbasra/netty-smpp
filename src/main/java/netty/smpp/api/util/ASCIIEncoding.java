package netty.smpp.api.util;

import java.io.UnsupportedEncodingException;

/**
 * Encoding class representing the ASCII (IA5) alphabet encoding.
 */
public class ASCIIEncoding extends netty.smpp.api.util.AlphabetEncoding {
    private static final int DCS = 1;
    private static final ASCIIEncoding INSTANCE = new ASCIIEncoding();

    /**
     * Construct a new ASCIIEncoding.
     */
    public ASCIIEncoding() {
        super(DCS);
        try {
            setCharset("US-ASCII");
        } catch (UnsupportedEncodingException x) {
            // All JVMs are required to support ASCII..
        }
    }

    /**
     * Get the singleton instance of ASCIIEncoding.
     *
     * @deprecated
     */
    public static ASCIIEncoding getInstance() {
        return INSTANCE;
    }
}

