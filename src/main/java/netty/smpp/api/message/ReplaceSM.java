package netty.smpp.api.message;

import netty.smpp.api.Address;
import netty.smpp.api.util.GSMConstants;
import netty.smpp.api.util.InvalidDateFormatException;
import netty.smpp.api.util.SMPPDate;
import netty.smpp.api.util.SMPPIO;

import java.io.OutputStream;

/**
 * Replace a message. This message submits a short message to the SMSC replacing
 * a previously submitted message. Relevant inherited fields from SMPPPacket:
 * <br>
 * <ul>
 * messageId source deliveryTime expiryTime registered defaultMsg message
 * </ul>
 *
 * @author Oran Kelly
 * @version 1.0
 */
public class ReplaceSM extends netty.smpp.api.message.SMPPRequest {
    /**
     * Construct a new ReplaceSM.
     */
    public ReplaceSM() {
        super(REPLACE_SM);
    }

    /**
     * Construct a new ReplaceSM with specified sequence number.
     *
     * @param seqNum The sequence number to use
     * @deprecated
     */
    public ReplaceSM(int seqNum) {
        super(REPLACE_SM, seqNum);
    }

    public int getBodyLength() {
        int len = ((messageId != null) ? messageId.length() : 0)
                + ((source != null) ? source.getLength() : 3)
                + ((deliveryTime != null) ? deliveryTime.toString().length() : 0)
                + ((expiryTime != null) ? expiryTime.toString().length() : 0)
                + ((message != null) ? message.length : 0);

        // 3 1-byte integers, 3 c-strings
        return len + 3 + 3;
    }

    /**
     * Write a byte representation of this packet to an OutputStream
     *
     * @param out The OutputStream to write to
     * @throws java.io.IOException if there's an error writing to the output stream.
     */
    protected void encodeBody(OutputStream out) throws java.io.IOException {
        int smLength = 0;
        if (message != null) {
            smLength = message.length;
        }

        SMPPIO.writeCString(getMessageId(), out);
        if (source != null) {
            source.writeTo(out);
        } else {
            // Write ton=0(null), npi=0(null), address=\0(nul)
            new Address(GSMConstants.GSM_TON_UNKNOWN,
                    GSMConstants.GSM_NPI_UNKNOWN, "").writeTo(out);
        }

        String dt = (deliveryTime == null) ? null : deliveryTime.toString();
        String et = (expiryTime == null) ? null : expiryTime.toString();

        SMPPIO.writeCString(dt, out);
        SMPPIO.writeCString(et, out);
        SMPPIO.writeInt(registered, 1, out);
        SMPPIO.writeInt(defaultMsg, 1, out);
        SMPPIO.writeInt(smLength, 1, out);
        if (message != null) {
            out.write(message);
        }
    }

    public void readBodyFrom(byte[] body, int offset)
            throws SMPPProtocolException {
        try {
            int smLength = 0;
            String delivery;
            String valid;

            messageId = SMPPIO.readCString(body, offset);
            offset += messageId.length() + 1;

            source = new Address();
            source.readFrom(body, offset);
            offset += source.getLength();

            delivery = SMPPIO.readCString(body, offset);
            offset += delivery.length() + 1;
            if (delivery.length() > 0) {
                deliveryTime = SMPPDate.parseSMPPDate(delivery);
            }

            valid = SMPPIO.readCString(body, offset);
            offset += valid.length() + 1;
            if (valid.length() > 0) {
                expiryTime = SMPPDate.parseSMPPDate(valid);
            }

            registered = SMPPIO.bytesToInt(body, offset++, 1);
            defaultMsg = SMPPIO.bytesToInt(body, offset++, 1);
            smLength = SMPPIO.bytesToInt(body, offset++, 1);

            if (smLength > 0) {
                message = new byte[smLength];
                System.arraycopy(body, offset, message, 0, smLength);
            }
        } catch (InvalidDateFormatException x) {
            throw new SMPPProtocolException("Unrecognized date format", x);
        }
    }

    /**
     * Convert this packet to a String. Not to be interpreted programmatically,
     * it's just dead handy for debugging!
     */
    public String toString() {
        return new String("replace_sm");
    }
}

