package netty.smpp.traditional;

import netty.smpp.api.Connection;
import netty.smpp.api.event.ConnectionObserver;
import netty.smpp.api.event.SMPPEvent;
import netty.smpp.api.message.BindResp;
import netty.smpp.api.message.SMPPPacket;

public class SmppReceiver implements ConnectionObserver {

    private Connection myConnection;

    private SmppReceiverConfig config = new SmppReceiverConfig();

    public static void main(String... args) {
        SmppReceiver receiver = new SmppReceiver();
        receiver.connect();
    }

    public void connect() {
        try {

            myConnection = new Connection(config.getHost(), config.getPort(), true);
            myConnection.addObserver(this);

            // Automatically respond to ENQUIRE_LINK requests from the SMSC
            myConnection.autoAckLink(false);
            myConnection.autoAckMessages(false);

            // Bind to the SMSC
            System.out.println("Binding to the SMSC.");

            synchronized (this) {
                BindResp resp = myConnection.bind(
                        Connection.RECEIVER,
                        config.getSystemId(),
                        config.getPassword(),
                        config.getSystemType(),
                        config.getBindTON(),
                        config.getBindNPI(),
                        config.getAddressRange());

                System.out.println("Waiting for unbind...");
                wait();
            }
            myConnection.closeLink();
        } catch (Exception ex) {
            System.out.println("Exception while receiver trying to connect: " + ex);
        }
    }

    @Override
    public void packetReceived(Connection myConnection, SMPPPacket smppPacket) {
        switch (smppPacket.getCommandId()) {

            // Bind transmitter response. Check it's status for success...
            case SMPPPacket.BIND_RECEIVER_RESP:
                if (smppPacket.getCommandStatus() != 0) {
                    System.out.println("Error binding to the SMSC. Error = " + smppPacket.getCommandStatus());
                } else {
                    System.out.println("Successfully bound!");
                }
                break;

            default:
                System.out.println("Unexpected packet received! Id = 0x" + Integer.toHexString(smppPacket.getCommandId()));
        }
    }

    @Override
    public void update(Connection r, SMPPEvent ev) {
        switch (ev.getType()) {
            case SMPPEvent.RECEIVER_EXIT:
                System.out.println("Receive exit");
                break;
        }
    }
}
