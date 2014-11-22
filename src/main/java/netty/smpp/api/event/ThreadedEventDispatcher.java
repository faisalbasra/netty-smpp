package netty.smpp.api.event;


import netty.smpp.api.Connection;
import netty.smpp.api.message.SMPPPacket;
import netty.smpp.api.util.APIConfig;
import netty.smpp.api.util.PropertyNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * An event dispatcher that does not block the receiver daemon thread.
 * <p>
 * <font size="+2"> <b>This class is highly experimental </b> </font>.
 * </p>
 * This dispatcher class has a pool of dispatcher threads. When an event is
 * delivered to this dispatcher by the receiver thread, it is added to a queue
 * and the method returns immediately. One dispatcher thread is then selected
 * and that thread is responsible for delivering the event to all registered
 * observers. The receiver daemon is then free to continue adding new events to
 * the event queue, which will be processed by a thread in the thread pool.
 *
 * @author Oran Kelly
 * @version $Id: ThreadedEventDispatcher.java 267 2006-03-09 16:37:31Z orank $
 */
public class ThreadedEventDispatcher implements netty.smpp.api.event.EventDispatcher, Runnable {

    private static final Log LOGGER = LogFactory.getLog(ThreadedEventDispatcher.class);

    /**
     * Runner flag. If set to false, all dispatcher threads will exit on next
     * iteration (some may be blocked on the queue).
     */
    private boolean running = true;

    /**
     * Size of the thread pool.
     */
    private int poolSize;

    /**
     * Pool of event dispatcher threads.
     */
    private ThreadGroup threadPool = new ThreadGroup("DispatcherPool");

    /**
     * FIFO queue of packets and SMPP events.
     */
    private netty.smpp.api.event.FIFOQueue queue;

    /**
     * Number of threads currently blocked on the queue.
     */
    private int threadsWaiting;

    /**
     * List of observers registered for event delivery.
     */
    private List observers = new ArrayList();

    /**
     * Create a new threaded event dispatcher object.
     */
    public ThreadedEventDispatcher() {
    }

    /**
     * Initialise this event dispatcher. This method will retrieve the size of
     * the thread pool and FIFO queue from the API configuration and initialise
     * both. See {@link netty.smpp.api.util.APIConfig}class documentation for the
     * appropriate configuration properties to use. If the properties are not
     * found in the configuration, the current defaults are a thread pool size
     * of <code>3</code> and a FIFO queue size of <code>100</code>.
     */
    public void init() {
        int queueSize;
        try {
            APIConfig cfg = APIConfig.getInstance();
            poolSize = cfg.getInt(APIConfig.EVENT_THREAD_POOL_SIZE);
            queueSize = cfg.getInt(APIConfig.EVENT_THREAD_FIFO_QUEUE_SIZE);
        } catch (PropertyNotFoundException x) {
            poolSize = 3;
            queueSize = 100;
        }

        // The queue must be created before the thread pool is initialised!
        queue = new netty.smpp.api.event.FIFOQueue(queueSize);
        initialiseThreadPool();
    }

    private void initialiseThreadPool() {
        Thread t;
        for (int i = 0; i < poolSize; i++) {
            t = new Thread(threadPool, this, "EventDispatch" + i);
            t.start();
        }
    }

    /**
     * Shut down all threads in the thread pool. This method will block until
     * all threads have terminated properly. Applications should be careful not
     * to use one of the thread pool's own threads to call this method as this
     * will cause a runtime exception. How can this method wait for all the
     * pool's threads to die if one of the pool's threads is executing this
     * method?
     */
    public void destroy() {
        LOGGER.debug("Shutting down dispatch threads.");

        // This could happen if an application attempts to set a new event
        // dispatcher during event processing. There are probably many other
        // ways this call-back could happen but it shouldn't!
        if (Thread.currentThread().getThreadGroup() == threadPool) {
            LOGGER.error("Cannot shut down the thread pool with one of it's own threads.");
            throw new RuntimeException();
        }

        running = false;
        synchronized (queue) {
            queue.notifyAll();
        }

        LOGGER.info("Waiting for threads in pool to die.");
        final int waitTime = 50;
        // Allow a full second of waiting!
        final int times = 1000 / waitTime;
        int time = 0;
        Thread[] pool = new Thread[poolSize];
        while (true) {
            try {
                pool[0] = null;
                threadPool.enumerate(pool, false);

                if (pool[0] == null) {
                    break;
                } else {
                    LOGGER.debug("There's still some threads running. Doing another loop..");
                }

                // Break out if it looks like we're stuck in an infinite loop
                if (time >= times) {
                    break;
                }

                // What's a good time to wait for more threads to terminate?
                Thread.sleep(waitTime);
                synchronized (queue) {
                    queue.notifyAll();
                }
            } catch (InterruptedException x) {
                threadPool.interrupt();
                Thread.yield();
            }
        }
        if (threadPool.activeCount() > 0) {
            LOGGER.error(threadPool.activeCount()
                    + " dispatcher threads refused to die.");
            if (LOGGER.isDebugEnabled()) {
                Thread[] threads = new Thread[threadPool.activeCount()];
                threadPool.enumerate(threads, false);
                for (int i = 0; i < pool.length; i++) {
                    LOGGER.debug(pool[i].getName());
                }
            }
        }
    }

    public void addObserver(netty.smpp.api.event.ConnectionObserver observer) {
        synchronized (observers) {
            if (!observers.contains(observer)) {
                observers.add(observer);
            }
        }
    }

    public void removeObserver(netty.smpp.api.event.ConnectionObserver observer) {
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    public Iterator observerIterator() {
        return Collections.unmodifiableList(observers).iterator();
    }

    public boolean contains(netty.smpp.api.event.ConnectionObserver observer) {
        synchronized (observers) {
            return observers.contains(observer);
        }
    }

    // notifyObservers is always single-threaded access as there's only 1
    // receiver thread!
    public void notifyObservers(Connection conn, SMPPEvent e) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Notifying observers of a new SMPP event "
                    + e.getType());
        }

        queue.put(conn, e);
        if (threadsWaiting > 0) {
            synchronized (queue) {
                queue.notify();
            }
        }
    }

    public void notifyObservers(Connection conn, SMPPPacket pak) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Notifying observers of a new SMPP packet ("
                    + Integer.toHexString(pak.getCommandId()) + ","
                    + Integer.toString(pak.getSequenceNum()) + ")");
        }
        queue.put(conn, pak);
        if (threadsWaiting > 0) {
            synchronized (queue) {
                queue.notify();
            }
        }
    }

    public void run() {
        netty.smpp.api.event.NotificationDetails nd;
        netty.smpp.api.event.ConnectionObserver observer;

        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Thread start: " + Thread.currentThread().getName());
            }

            while (running) {
                nd = null;
                try {
                    synchronized (queue) {
                        if (queue.isEmpty()) {
                            threadsWaiting++;
                            queue.wait();
                            threadsWaiting--;
                        }

                        nd = queue.get();
                    }
                } catch (InterruptedException x) {
                    continue;
                }

                if (nd == null) {
                    continue;
                }

                for (int i = observers.size() - 1; i >= 0; i--) {
                    observer = (ConnectionObserver) observers.get(i);
                    if (nd.hasEvent()) {
                        observer.packetReceived(
                                nd.getConnection(), nd.getPacket());
                    } else {
                        observer.update(
                                nd.getConnection(), nd.getEvent());
                    }
                }
            } // end while

            LOGGER.debug("Thread exit: " + Thread.currentThread().getName());
        } catch (Exception x) {
            LOGGER.warn("Exception in dispatcher thread", x);
        }
    }
}

