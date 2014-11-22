package netty.smpp.api;

/**
 * SMPPException.
 *
 * @author Oran Kelly
 * @version 1.0
 */
public class SMPPException extends Exception {
    static final long serialVersionUID = -5382146274442716891L;

    public SMPPException() {
        super();
    }

    public SMPPException(String s) {
        super(s);
    }

    public SMPPException(String msg, Throwable rootCause) {
        super(msg, rootCause);
    }
}

