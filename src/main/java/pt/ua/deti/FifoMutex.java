package pt.ua.deti;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * Basic implementation of a {@link Mutex}.
 * <p>
 * It uses a {@link Queue} to store the waiting {@link Thread} in order, 
 * and an {@link AtomicBoolean} to implement mutual exclusion.
 * It also uses the {@link LockSupport} class to park (suspend) and unpark (resume) a thread.
 * 
 * @author Catarina Silva
 * @version 1.0
 */
class FifoMutex implements Mutex {
  private final AtomicBoolean locked = new AtomicBoolean(false);
  private final Queue<Thread> waiters = new ConcurrentLinkedQueue<>();

  @Override
  public final void lock() {
    boolean wasInterrupted = false;
    Thread t = Thread.currentThread();
    // publish current thread for unparkers
    waiters.add(t);

    // Block while not first in queue or cannot acquire lock
    while (waiters.peek() != t || !locked.compareAndSet(false, true)) {
      LockSupport.park(t);
      // ignore interrupts while waiting
      if (Thread.interrupted())
        wasInterrupted = true;
    }

    waiters.remove();
    // ensure correct interrupt status on return
    if (wasInterrupted)
      t.interrupt();
  }

  @Override
  public final void unlock() {
    locked.set(false);
    LockSupport.unpark(waiters.peek());
  }

  /**
   * Reduces the risk of "lost unpark" due to classloading
   */
  static { Class<?> ensureLoaded = LockSupport.class; }
}