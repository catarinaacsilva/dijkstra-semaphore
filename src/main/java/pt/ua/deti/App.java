package pt.ua.deti;

import java.util.LinkedList;
import java.util.Queue;

public class App {
    public static void main(final String[] args) {
        final Semaphore emptyCount = new DijkstraSem(1);
        final Semaphore useQueue = new DijkstraSem(1);
        final Semaphore fullCount = new DijkstraSem(0);

        final Queue<String> queue = new LinkedList<>();

        final Thread iIncrement = new Thread(new Producer(emptyCount, useQueue, fullCount, queue));
        final Thread iDecrement = new Thread(new Consumer(emptyCount, useQueue, fullCount, queue));

        iIncrement.start();
        iDecrement.start();
    }

    public static class Producer implements Runnable {
        private final Semaphore emptyCount;
        private final Semaphore useQueue;
        private final Semaphore fullCount;
        private final Queue<String> queue;

        public Producer(final Semaphore e, final Semaphore u, final Semaphore f, final Queue<String> q) {
            this.emptyCount = e;
            this.useQueue = u;
            this.fullCount = f;
            this.queue = q;
        }

        @Override
        public void run() {
            String txt = "As armas e os Barões assinalados";
            String tokens[] = txt.split("\\s");

            for (String token : tokens) {
                try {
                    emptyCount.down();
                    useQueue.down();
                    queue.add(token);
                    useQueue.up();
                    fullCount.up();
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer implements Runnable {
        private final Semaphore emptyCount;
        private final Semaphore useQueue;
        private final Semaphore fullCount;
        private final Queue<String> queue;

        public Consumer(final Semaphore e, final Semaphore u, final Semaphore f, final Queue<String> q) {
            this.emptyCount = e;
            this.useQueue = u;
            this.fullCount = f;
            this.queue = q;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    fullCount.down();
                    useQueue.down();
                    String txt = queue.remove();
                    useQueue.up();
                    emptyCount.up();
                    System.out.println(txt);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
