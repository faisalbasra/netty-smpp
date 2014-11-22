package netty.smpp.api.message;

import netty.smpp.api.Address;

import java.io.OutputStream;

/**
 * Alert notification. This packet type is sent from the SMSC to an ESME to
 * signal that a particular mobile subscriber has become available and a
 * delivery pending flag had previously been set for that subscriber by a
 * <code>data_sm</code> packet.
 * <p/>
 * Note that there is no response packet to an <code>alert_notification</code>.
 *
 * @author Oran Kelly
 * @version 1.0
 */
public class AlertNotification extends netty.smpp.api.message.SMPPRequest {
    /**
     * Create a new alert_notification object.
     */
    public AlertNotification() {
        super(ALERT_NOTIFICATION);
    }

    /**
     * Create a new alert_notification object with sequence number
     * <code>seqNum</code>.
     */
    public AlertNotification(int seqNum) {
        super(ALERT_NOTIFICATION, seqNum);
    }

    public int getBodyLength() {
        return ((source != null) ? source.getLength() : 3)
                + ((destination != null) ? destination.getLength() : 3);
    }

    public void encodeBody(OutputStream out) throws java.io.IOException {
        if (source != null) {
            source.writeTo(out);
        } else {
            new Address().writeTo(out);
        }

        if (destination != null) {
            destination.writeTo(out);
        } else {
            new Address().writeTo(out);
        }
    }

    public void readBodyFrom(byte[] body, int offset)
            throws SMPPProtocolException {
        source = new Address();
        source.readFrom(body, offset);
        offset += source.getLength();

        destination = new Address();
        destination.readFrom(body, offset);
    }

    public String toString() {
        return "alert_notification";
    }
}

