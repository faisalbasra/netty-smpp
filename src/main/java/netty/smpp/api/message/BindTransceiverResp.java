package netty.smpp.api.message;

/**
 * SMSC response to a BindTransceiver request.
 *
 * @author Oran Kelly
 * @version 1.0
 */
public class BindTransceiverResp extends netty.smpp.api.message.BindResp {
    /**
     * Construct a new BindTransceiverResp.
     */
    public BindTransceiverResp() {
        super(BIND_TRANSCEIVER_RESP);
    }

    /**
     * Create a new BindTransceiverResp packet in response to a BindTransceiver.
     * This constructor will set the sequence number to that of the packet it is
     * in response to.
     *
     * @param r The Request packet the response is to
     */
    public BindTransceiverResp(netty.smpp.api.message.BindTransceiver r) {
        super(r);
    }

    /**
     * Convert this packet to a String. Not to be interpreted programmatically,
     * it's just dead handy for debugging!
     */
    public String toString() {
        return new String("bind_transceiver_resp");
    }
}

