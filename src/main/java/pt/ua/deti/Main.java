package pt.ua.deti;

import java.io.Console;
import java.util.LinkedList;
import java.util.Queue;

/**
 * The main execution program.
 * <p>
 * It implements a basic Producer/Consumer problem.<br>
 * The user input a sentence, which is splitted into tokens.<br>
 * Then the tokens are shared between the {@link Producer} and {@link Consumer}.
 * 
 * @see <a href=
 *      "https://en.wikipedia.org/wiki/Producer%E2%80%93consumer_problem">Producer–consumer
 *      problem</a>
 * 
 * @author Catarina Silva
 * @version 1.0
 */
public class Main {
    public static void main(final String[] args) {
        final Semaphore emptyCount = new DijkstraSem(1);
        final Semaphore useQueue = new DijkstraSem(1);
        final Semaphore fullCount = new DijkstraSem(0);
        final Queue<String> queue = new LinkedList<>();

        String txt = "As armas e os Barões assinalados";

        // Read a string from
        try {
            final Console console = System.console();
            // if console is not null
            if (console != null) {
                // read line from the user input
                final String inputString = console.readLine("Sentence: ");
                if (inputString.length() > 0) {
                    txt = inputString;
                }
            }
        } catch (final Exception ex) {

        }

        final Thread iIncrement = new Thread(new Producer(txt, emptyCount, useQueue, fullCount, queue));
        final Thread iDecrement = new Thread(new Consumer(emptyCount, useQueue, fullCount, queue));

        iIncrement.start();
        iDecrement.start();

        try {
            iDecrement.join();
            iIncrement.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
