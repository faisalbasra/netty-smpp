package netty.smpp.api;

/**
 * AlreadyBoundException
 *
 * @author Oran Kelly
 * @version 1.0
 */
public class AlreadyBoundException extends netty.smpp.api.SMPPRuntimeException {
    static final long serialVersionUID = 6996870876381065702L;

    public AlreadyBoundException() {
    }

    /**
     * Construct a new AlreadyBoundException with specified message.
     */
    public AlreadyBoundException(String s) {
        super(s);
    }
}

