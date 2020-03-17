package pt.ua.deti;

/**
 * {@link Mutex} implementation provides {@link Mutex#lock()} and {@link Mutex#unlock()} methods.
 * 
 * 
 * @author Catarina Silva
 * @version 1.0
 */
public interface Mutex {
    /**
     * Acquires the lock.
     * <p>
     * If the lock is not available then the current thread becomes disabled for thread scheduling 
     * purposes and lies dormant until the lock has been acquired.
     */
    public void lock();
    
    /**
     * Releases the lock.
     */
    public void unlock();
}