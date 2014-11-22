package netty.smpp.api.util;


public abstract class MessageEncoding {

    private int dataCoding = -1;

    protected MessageEncoding(int dataCoding) {
        this.dataCoding = dataCoding;
    }

    /**
     * Get the correct data_coding value for this message encoding type.
     */
    public final int getDataCoding() {
        return dataCoding;
    }

    public int getEncodingLength() {
        return 8;
    }
}

