package netty.smpp.api.message.tlv;

/**
 * Attempt to set a value on a tag that expects a Java type other than that
 * used. This exception gets thrown if an attempt is made, for instance, to set
 * a <code>java.lang.String</code> value on a Tag that is defined as an
 * integer.
 *
 * @version $Id: BadValueTypeException.java 258 2006-03-09 11:37:09Z orank $
 */
public class BadValueTypeException extends RuntimeException {
    static final long serialVersionUID = 5894340962605773779L;

    /**
     * Create a new BadValueTypeException.
     */
    public BadValueTypeException() {
    }

    /**
     * Create a new BadValueTypeException.
     *
     * @param msg Exception message.
     */
    public BadValueTypeException(String msg) {
        super(msg);
    }
}

