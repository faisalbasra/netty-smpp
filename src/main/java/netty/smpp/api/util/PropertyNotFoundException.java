package netty.smpp.api.util;

public class PropertyNotFoundException extends netty.smpp.api.SMPPRuntimeException {
    static final long serialVersionUID = -3513175897407921550L;

    private String property = "";

    public PropertyNotFoundException() {
    }

    public PropertyNotFoundException(String property) {
        super();
        this.property = property;
    }

    public String getProperty() {
        return property;
    }
}

