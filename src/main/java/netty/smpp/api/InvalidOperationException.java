package netty.smpp.api;

/**
 * InvalidOperationException
 *
 * @author Oran Kelly
 * @version 1.0
 */
public class InvalidOperationException extends netty.smpp.api.SMPPException {
    static final long serialVersionUID = 7624381507606078212L;

    public InvalidOperationException() {
    }

    /**
     * Construct a new InvalidOperationException with specified message.
     */
    public InvalidOperationException(String s) {
        super(s);
    }
}

