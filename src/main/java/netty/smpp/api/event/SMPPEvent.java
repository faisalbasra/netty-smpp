package netty.smpp.api.event;


import netty.smpp.api.Connection;

/**
 * Abstract super class of SMPP control events.
 *
 * @author Oran Kelly
 * @version $Id: SMPPEvent.java 264 2006-03-09 14:00:40Z orank $
 */
public abstract class SMPPEvent {
    /**
     * ReceiverStartEvent enumeration type.
     */
    public static final int RECEIVER_START = 2;

    /**
     * ReceiverExitEvent enumeration type.
     */
    public static final int RECEIVER_EXIT = 3;

    /**
     * ReceiverExceptionEvent enumeration type.
     */
    public static final int RECEIVER_EXCEPTION = 4;

    /**
     * The source Connection of this event.
     */
    private Connection source;

    /**
     * The type of this event.
     */
    private int type;

    /**
     * Construct a new event. The <code>type</code> parameter should match one
     * of the enumeration constants defined in this class.
     */
    protected SMPPEvent(int type, Connection source) {
        this.source = source;
        this.type = type;
    }

    /**
     * Get the source connection of this event.
     */
    public Connection getSource() {
        return source;
    }

    /**
     * Get the enumeration type of this event.
     *
     * @see #RECEIVER_EXIT
     * @see #RECEIVER_EXCEPTION
     */
    public int getType() {
        return type;
    }
}

