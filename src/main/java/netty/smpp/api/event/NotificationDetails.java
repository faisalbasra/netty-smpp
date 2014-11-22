package netty.smpp.api.event;


import netty.smpp.api.Connection;
import netty.smpp.api.message.SMPPPacket;

class NotificationDetails {
    private Connection connection;
    private netty.smpp.api.event.SMPPEvent event;
    private SMPPPacket packet;

    public NotificationDetails() {
    }

    public Connection getConnection() {
        return connection;
    }


    public void setConnection(Connection conn) {
        this.connection = conn;
    }


    public netty.smpp.api.event.SMPPEvent getEvent() {
        return event;
    }


    public void setEvent(netty.smpp.api.event.SMPPEvent event) {
        this.event = event;
    }


    public SMPPPacket getPacket() {
        return packet;
    }


    public void setPacket(SMPPPacket pak) {
        this.packet = pak;
    }


    public void setDetails(Connection c, netty.smpp.api.event.SMPPEvent e, SMPPPacket p) {
        connection = c;
        event = e;
        packet = p;
    }

    public boolean hasEvent() {
        return event != null;
    }
}
