package netty.smpp.api.event;

import netty.smpp.api.Connection;

/**
 * Event generated by the receiver thread when a non-fatal exception is caught.
 * An application will receive this event type if the receiver thread catches an
 * exception which does not cause it to terminate. The exception which was
 * caught and the state the connection was in when it was caught are saved in
 * this event.
 *
 * @author Oran Kelly
 * @version $Id: ReceiverExceptionEvent.java 264 2006-03-09 14:00:40Z orank $
 */
public class ReceiverExceptionEvent extends netty.smpp.api.event.SMPPEvent {
    /**
     * The exception that was caught.
     */
    private Throwable exception;

    /**
     * The state the Connection was in when the exception was caught.
     */
    private int connectionState;

    /**
     * Create a new ReceiverExceptionEvent.
     *
     * @param source the source Connection of this event.
     * @param t      the exception being reported.
     */
    public ReceiverExceptionEvent(Connection source, Throwable t) {
        super(RECEIVER_EXCEPTION, source);
        this.exception = t;
    }

    /**
     * Create a new ReceiverExceptionEvent.
     *
     * @param source the source Connection of this event.
     * @param t      the exception being reported.
     * @param state  the state the connection was in when the exception was caught.
     */
    public ReceiverExceptionEvent(Connection source, Throwable t, int state) {
        super(RECEIVER_EXCEPTION, source);
        this.exception = t;
        this.connectionState = state;
    }

    /**
     * Get the exception which was caught.
     */
    public Throwable getException() {
        return exception;
    }

    /**
     * Get the state the connection was in when the exception was caught.
     *
     * @return the integer value representing the state of the connection.
     * @see netty.smpp.api.Connection#BOUND
     * @see netty.smpp.api.Connection#UNBOUND
     * @see netty.smpp.api.Connection#BINDING
     * @see netty.smpp.api.Connection#UNBINDING
     */
    public int getState() {
        return connectionState;
    }
}
