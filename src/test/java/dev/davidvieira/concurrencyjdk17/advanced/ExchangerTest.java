package dev.davidvieira.concurrencyjdk17.advanced;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ExchangerTest {

    @Test
    @DisplayName("Using Exchanger to Exchange Data Between Threads")
    void testExchanger() throws InterruptedException {
        Exchanger<String> exchanger = new Exchanger<>();

        Runnable task1 = () -> {
            try {
                String message = exchanger.exchange("Message from Thread 1");
                assertEquals("Message from Thread 2", message);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Runnable task2 = () -> {
            try {
                String message = exchanger.exchange("Message from Thread 2");
                assertEquals("Message from Thread 1", message);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }
}
