package dev.davidvieira.concurrencyjdk17;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ManagingThreadLifeCycleTest {

    @Test
    @DisplayName("Test Polling with Sleep")
    void testPollingWithSleep() throws InterruptedException {
        // Creating a thread that increments a counter
        class Counter {
            private int count = 0;

            public void increment() {
                count++;
            }

            public int getCount() {
                return count;
            }
        }

        Counter counter = new Counter();
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                counter.increment();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        thread.start();
        thread.join();

        // Asserting that the counter has reached 10
        assertEquals(10, counter.getCount());
    }

    @Test
    @DisplayName("Test Interrupting a Thread")
    void testInterruptingAThread() throws InterruptedException {
        // Creating a thread that waits for interruption
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread was interrupted!");
            }
        });

        thread.start();
        thread.interrupt();
        thread.join();

        // Asserting that the thread was interrupted
        assertFalse(thread.isAlive());
    }
}
