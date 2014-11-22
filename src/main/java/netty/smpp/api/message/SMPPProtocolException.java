package netty.smpp.api.message;

public class SMPPProtocolException extends netty.smpp.api.SMPPRuntimeException {
    static final long serialVersionUID = -7206604214368878502L;

    public SMPPProtocolException(String msg) {
        super(msg);
    }

    public SMPPProtocolException(String msg, Throwable rootCause) {
        super(msg, rootCause);
    }
}

