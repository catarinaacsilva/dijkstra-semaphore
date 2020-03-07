package pt.ua.deti;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.LockSupport;

/**
 * 
 */
public class DijkstraSem implements Semaphore {
    private int value;
    private final Mutex lock = new FifoMutex();
    private final Queue<Thread> waiters = new ConcurrentLinkedQueue<>();

    /**
     * 
     */
    public DijkstraSem()
    {
        value = 0;
    }

    /**
     * 
     * @param value
     */
    public DijkstraSem(final int value) {

        this.value = value;
    }

    @Override
    public void down() throws InterruptedException {
        lock.lock();
        value -= 1;
        if(value < 0) {
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
    public synchronized void up() throws InterruptedException {
        lock.lock();
        value += 1;
        if(value <= 0) {
            Thread next = waiters.remove();
            LockSupport.unpark(next);
        }
        lock.unlock();
    }
}