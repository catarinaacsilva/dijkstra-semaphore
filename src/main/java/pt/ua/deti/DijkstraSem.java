package pt.ua.deti;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.LockSupport;

/**
 * A basic implementation of a {@link Semaphore}.
 * <p>
 * It uses the {@link Mutex} to implement mutual exclusion, and the
 * {@link LockSupport} class to park (suspend) and unpark (resume) a thread. It
 * also uses a {@link Queue} to store the {@link Thread} that are currently
 * waiting.
 * 
 * @author Catarina Silva
 * @version 1.0
 */
public class DijkstraSem implements Semaphore {
    private int value;
    private final Mutex lock = new FifoMutex();
    private final Queue<Thread> waiters = new ConcurrentLinkedQueue<>();

    /**
     * A default constructor that initiate the {@link Semaphore} with 0.
     */
    public DijkstraSem() {
        value = 0;
    }

    /**
     * Initiate the {@link Semaphore} with {@code value}.
     * 
     * @param value the initial number of permits for the {@link Semaphore}
     */
    public DijkstraSem(final int value) {
        this.value = value;
    }

    @Override
    public void down() {
        lock.lock();
        value -= 1;
        if (value < 0) {
            Thread current = Thread.currentThread();
            waiters.add(current);
            lock.unlock();
            LockSupport.park(this);
            if (Thread.interrupted()) {
                current.interrupt();
            }
        } else {
            lock.unlock();
            return;
        }
    }

    @Override
    public synchronized void up() {
        lock.lock();
        value += 1;
        if (value <= 0) {
            Thread next = waiters.remove();
            LockSupport.unpark(next);
        }
        lock.unlock();
    }

    /**
     * Reduces the risk of "lost unpark" due to classloading
     */
    static {
        Class<?> ensureLoaded = LockSupport.class;
    }
}