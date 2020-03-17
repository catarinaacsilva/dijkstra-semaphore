package pt.ua.deti;

import static org.junit.Assert.assertArrayEquals;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import org.junit.Test;

/**
 * Unit Test for the {@link Semaphore}.
 * 
 * @author Catarina Silva
 * @version 1.0
 */
public class MainTest {

    /**
     * Randomly permutes the specified list using a default source of
     * randomness.<br>
     * All permutations occur with approximately equal likelihood.
     * 
     * @param array the array which content will be shuffled
     */
    public static void shuffle(final int array[]) {
        Random rand = new Random();

        for (int i = 0; i < array.length; i++) {
            int randomIndexToSwap = rand.nextInt(array.length);
            int temp = array[randomIndexToSwap];
            array[randomIndexToSwap] = array[i];
            array[i] = temp;
        }
    }

    @Test
    public void producerConsumer() {
        final int numbers[] = { 1, 2, 3, 4, 5, 6, 7, 8, 9 }, output[] = new int[numbers.length];
        // randomly shuffle all the numbers
        shuffle(numbers);

        // instantiate the necessary semaphores
        final Semaphore emptyCount = new DijkstraSem(1);
        final Semaphore useQueue = new DijkstraSem(1);
        final Semaphore fullCount = new DijkstraSem(0);
        final Queue<Integer> queue = new LinkedList<>();

        Producer p = new Producer(numbers, emptyCount, useQueue, fullCount, queue);
        Consumer c = new Consumer(output, emptyCount, useQueue, fullCount, queue);

        final Thread iIncrement = new Thread(p);
        final Thread iDecrement = new Thread(c);

        iIncrement.start();
        iDecrement.start();

        try {
            iDecrement.join();
            iIncrement.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertArrayEquals(numbers, output);
    }

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
        private final Queue<Integer> queue;
        /**
         * Array of numbers
         */
        private final int array[];

        /**
         * The default constructor.
         * 
         * @param a Array of numbers
         * @param e {@link Semaphore} that counts the empty positions
         * @param u {@link Semaphore} that garantees mutual exclusion
         * @param f {@link Semaphore} that counts the use positions
         * @param q {@link Queue} used for sharing data
         */
        public Consumer(final int a[], final Semaphore e, final Semaphore u, final Semaphore f,
                final Queue<Integer> q) {
            this.array = a;
            this.emptyCount = e;
            this.useQueue = u;
            this.fullCount = f;
            this.queue = q;
        }

        /**
         * Retuns the numbers that the consumer received.
         * 
         * @return the numbers that the consumer received
         */
        public final int[] getNumbers() {
            return array;
        }

        @Override
        public void run() {
            int i = 0;
            boolean done = false;
            while (!done) {
                try {
                    fullCount.down();
                    useQueue.down();
                    int number = queue.remove();
                    useQueue.up();
                    emptyCount.up();
                    // checks if the token is the stop character
                    if (number == 0) {
                        done = true;
                    } else {
                        array[i++] = number;
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

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
        private final Queue<Integer> queue;
        /**
         * Array of numbers
         */
        private final int array[];

        /**
         * The default constructor.
         * 
         * @param a Array of numbers
         * @param e {@link Semaphore} that counts the empty positions
         * @param u {@link Semaphore} that garantees mutual exclusion
         * @param f {@link Semaphore} that counts the use positions
         * @param q {@link Queue} used for sharing data
         */
        public Producer(final int a[], final Semaphore e, final Semaphore u, final Semaphore f,
                final Queue<Integer> q) {
            this.array = a;
            this.emptyCount = e;
            this.useQueue = u;
            this.fullCount = f;
            this.queue = q;
        }

        @Override
        public void run() {

            // add all the tokens to the queue
            for (int number : array) {
                emptyCount.down();
                useQueue.down();
                queue.add(number);
                useQueue.up();
                fullCount.up();
            }

            // add a a special token to stop the consumer
            emptyCount.down();
            useQueue.down();
            queue.add(0);
            useQueue.up();
            fullCount.up();
        }
    }
}
