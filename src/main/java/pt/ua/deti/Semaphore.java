package pt.ua.deti;

/**
 * {@link Semaphore} implementation provides {@link Semaphore#down()} and
 * {@link Semaphore#up()} methods.
 * 
 * @author Catarina Silva
 * @version 1.0
 */
public interface Semaphore {
    /**
     * Acquires a permit from this semaphore, blocking until one is available, or
     * the thread is interrupted.
     * <p>
     * Acquires a permit, if one is available and returns immediately, reducing the
     * number of available permits by one.
     * <p>
     * If no permit is available then the current thread becomes disabled for thread
     * scheduling purposes and lies dormant until one of two things happens:
     * <ul>
     * <li>Some other thread invokes the release() method for this semaphore and the
     * current thread is next to be assigned a permit; or
     * <li>Some other thread interrupts the current thread.
     * </ul>
     * <p>
     * If the current thread:
     * <ul>
     * <li>has its interrupted status set on entry to this method; or
     * <li>is interrupted while waiting for a permit,
     * </ul>
     * <p>
     * then {@link InterruptedException} is thrown and the current thread's
     * interrupted status is cleared.
     */
    public void down();

    /**
     * Releases a permit, returning it to the semaphore.
     * <p>
     * Releases a permit, increasing the number of available permits by one. If any
     * threads are trying to acquire a permit, then one is selected and given the
     * permit that was just released. That thread is (re)enabled for thread
     * scheduling purposes.
     * <p>
     * There is no requirement that a thread that releases a permit must have
     * acquired that permit by calling acquire(). Correct usage of a semaphore is
     * established by programming convention in the application.
     */
    public void up();
}