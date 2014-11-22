package netty.smpp.api.util;

import netty.smpp.api.BadCommandIDException;
import netty.smpp.api.message.*;

/**
 * Helper class to create new SMPP packet objects.
 *
 * @author Oran Kelly
 * @since 1.0
 */
public final class PacketFactory {
    private PacketFactory() {
    }

    /**
     * Create a new instance of the appropriate sub class of SMPPPacket.
     *
     * @deprecated
     */
    public static SMPPPacket newPacket(int id) throws BadCommandIDException {
        return newInstance(id);
    }

    /**
     * Create a new instance of the appropriate sub class of SMPPPacket. Packet
     * fields are all left at their default initial state.
     *
     * @param id The SMPP command ID of the packet type to return.
     * @return A sub-class instance of {@link netty.smpp.api.message.SMPPPacket}
     * representing SMPP command <code>id</code>.
     * @throws netty.smpp.api.BadCommandIDException if the command ID is not recognized.
     */
    public static SMPPPacket newInstance(int id) throws BadCommandIDException {
        SMPPPacket response = null;

        switch (id) {
            case SMPPPacket.GENERIC_NACK:
                response = new GenericNack();
                break;

            case SMPPPacket.BIND_RECEIVER:
                response = new BindReceiver();
                break;

            case SMPPPacket.BIND_RECEIVER_RESP:
                response = new BindReceiverResp();
                break;

            case SMPPPacket.BIND_TRANSMITTER:
                response = new BindTransmitter();
                break;

            case SMPPPacket.BIND_TRANSMITTER_RESP:
                response = new BindTransmitterResp();
                break;

            case SMPPPacket.BIND_TRANSCEIVER:
                response = new BindTransceiver();
                break;

            case SMPPPacket.BIND_TRANSCEIVER_RESP:
                response = new BindTransceiverResp();
                break;

            case SMPPPacket.UNBIND:
                response = new Unbind();
                break;

            case SMPPPacket.UNBIND_RESP:
                response = new UnbindResp();
                break;

            case SMPPPacket.SUBMIT_SM:
                response = new SubmitSM();
                break;

            case SMPPPacket.SUBMIT_SM_RESP:
                response = new SubmitSMResp();
                break;

            case SMPPPacket.DATA_SM:
                response = new DataSM();
                break;

            case SMPPPacket.DATA_SM_RESP:
                response = new DataSMResp();
                break;

            case SMPPPacket.ALERT_NOTIFICATION:
                response = new AlertNotification();
                break;

            case SMPPPacket.SUBMIT_MULTI:
                response = new SubmitMulti();
                break;

            case SMPPPacket.SUBMIT_MULTI_RESP:
                response = new SubmitMultiResp();
                break;

            case SMPPPacket.DELIVER_SM:
                response = new DeliverSM();
                break;

            case SMPPPacket.DELIVER_SM_RESP:
                response = new DeliverSMResp();
                break;

            case SMPPPacket.QUERY_SM:
                response = new QuerySM();
                break;

            case SMPPPacket.QUERY_SM_RESP:
                response = new QuerySMResp();
                break;

            case SMPPPacket.QUERY_LAST_MSGS:
                response = new QueryLastMsgs();
                break;

            case SMPPPacket.QUERY_LAST_MSGS_RESP:
                response = new QueryLastMsgsResp();
                break;

            case SMPPPacket.QUERY_MSG_DETAILS:
                response = new QueryMsgDetails();
                break;

            case SMPPPacket.QUERY_MSG_DETAILS_RESP:
                response = new QueryMsgDetailsResp();
                break;

            case SMPPPacket.CANCEL_SM:
                response = new CancelSM();
                break;

            case SMPPPacket.CANCEL_SM_RESP:
                response = new CancelSMResp();
                break;

            case SMPPPacket.REPLACE_SM:
                response = new ReplaceSM();
                break;

            case SMPPPacket.REPLACE_SM_RESP:
                response = new ReplaceSMResp();
                break;

            case SMPPPacket.ENQUIRE_LINK:
                response = new EnquireLink();
                break;

            case SMPPPacket.ENQUIRE_LINK_RESP:
                response = new EnquireLinkResp();
                break;

            case SMPPPacket.PARAM_RETRIEVE:
                response = new ParamRetrieve();
                break;

            case SMPPPacket.PARAM_RETRIEVE_RESP:
                response = new ParamRetrieveResp();
                break;

            default:
                throw new BadCommandIDException();
        }
        return response;
    }
}
