package netty.smpp.api.message.tlv;

/**
 * An attempt was made to encode or decode a value with a length outside of the
 * bounds defined by its <code>Tag</code>. This can happen, for instance,
 * when an attempt is made to encode a string value that is longer than the
 * maximum length defined by the tag for that value.
 *
 * @version $Id: InvalidSizeForValueException.java 258 2006-03-09 11:37:09Z orank $
 */
public class InvalidSizeForValueException extends RuntimeException {
    static final long serialVersionUID = -4600629750433218768L;

    /**
     * Create a new InvalidSizeForValueException.
     */
    public InvalidSizeForValueException() {
    }

    /**
     * Create a new InvalidSizeForValueException.
     *
     * @param msg The exception message.
     */
    public InvalidSizeForValueException(String msg) {
        super(msg);
    }
}
