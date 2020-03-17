package pt.ua.deti;

import java.util.Queue;

/**
 * A simple implementation of a Producer.
 * <p>
 * It uses {@link Semaphore} to garantee the mutual exclusion on a
 * {@link Queue}.
 * 
 * @author Catarina Silva
 * @version 1.0
 */
public class Producer implements Runnable {
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
     * {@link String} that will be splitted into tokens
     */
    private final String txt;

    /**
     * The default constructor.
     * 
     * @param t {@link String} that will be splitted into tokens
     * @param e {@link Semaphore} that counts the empty positions
     * @param u {@link Semaphore} that garantees mutual exclusion
     * @param f {@link Semaphore} that counts the use positions
     * @param q {@link Queue} used for sharing data
     */
    public Producer(final String t, final Semaphore e, final Semaphore u, final Semaphore f, final Queue<String> q) {
        this.txt = t;
        this.emptyCount = e;
        this.useQueue = u;
        this.fullCount = f;
        this.queue = q;
    }

    @Override
    public void run() {
        String tokens[] = txt.split("\\s");

        try {
            // add all the tokens to the queue
            for (String token : tokens) {
                emptyCount.down();
                useQueue.down();
                queue.add(token);
                useQueue.up();
                fullCount.up();
            }

            // add a a special token to stop the consumer
            emptyCount.down();
            useQueue.down();
            queue.add("\\u000D");
            useQueue.up();
            fullCount.up();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}