package netty.smpp.api.event;

import netty.smpp.api.Connection;

/**
 * Event generated when the receiver thread starts. Usually applications can
 * ignore this message as they don't need to do anything when the receiver
 * thread starts.
 *
 * @author Oran Kelly
 * @version $Id: ReceiverStartEvent.java 244 2006-01-22 21:56:28Z orank $
 */
public class ReceiverStartEvent extends netty.smpp.api.event.SMPPEvent {
    /**
     * Create a new ReceiverStartEvent.
     */
    public ReceiverStartEvent(Connection source) {
        super(RECEIVER_START, source);
    }
}

