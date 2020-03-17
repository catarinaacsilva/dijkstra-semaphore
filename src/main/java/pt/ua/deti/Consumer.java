package pt.ua.deti;

import java.util.Queue;

/**
 * A simple implementation of a Consumer.
 * <p>
 * It uses {@link Semaphore} to garantee the mutual exclusion on a
 * {@link Queue}.
 * 
 * @author Catarina Silva
 * @version 1.0
 */
public class Consumer implements Runnable {
    /**
     * {@link Semaphore} that counts the empty positions
     */
    private final Semaphore emptyCount;
    /**
     * {@link Semaphore} that garantees mutual exclusion
     */
    private final Semaphore useQueue;
    /**
     * {@link Semaphore} that counts the use positions
     */
    private final Semaphore fullCount;
    /**
     * {@link Queue} used for sharing data
     */
    private final Queue<String> queue;

    /**
     * The default constructor.
     * 
     * @param e {@link Semaphore} that counts the empty positions
     * @param u {@link Semaphore} that garantees mutual exclusion
     * @param f {@link Semaphore} that counts the use positions
     * @param q {@link Queue} used for sharing data
     */
    public Consumer(final Semaphore e, final Semaphore u, final Semaphore f, final Queue<String> q) {
        this.emptyCount = e;
        this.useQueue = u;
        this.fullCount = f;
        this.queue = q;
    }

    @Override
    public void run() {
        boolean done = false; 
        while (!done) {
            try {
                fullCount.down();
                useQueue.down();
                String token = queue.remove();
                useQueue.up();
                emptyCount.up();
                // checks if the token is the stop character
                if (token.equals("\\u000D")) {
                    done = true;   
                } else {
                    System.out.println(token);
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
}