package netty.smpp.api;

public class UnsupportedOperationException extends netty.smpp.api.SMPPRuntimeException {
    static final long serialVersionUID = 2200729955220317767L;

    public UnsupportedOperationException() {
    }

    public UnsupportedOperationException(String msg) {
        super(msg);
    }
}

