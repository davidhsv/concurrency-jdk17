package dev.davidvieira.concurrencyjdk17;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IntroducingThreadsTest {

    @Test
    @DisplayName("Create and Start a Thread Using Runnable")
    void testCreateAndStartThreadUsingRunnable() throws InterruptedException, ExecutionException {
        // Creating and starting a thread using Runnable
        Runnable task = () -> System.out.println("Hello from a thread!");
        Thread thread = new Thread(task);
        thread.start();

        // Asserting that the thread is alive
        assertTrue(thread.isAlive());

        // Waiting for the thread to finish
        thread.join();

        // Asserting that the thread is not alive after completion
        assertFalse(thread.isAlive());
    }

    @Test
    @DisplayName("Test Thread State Transitions")
    void testThreadStateTransitions() throws InterruptedException {
        // Creating a thread that sleeps for a short duration
        Runnable task = () -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
        Thread thread = new Thread(task);

        // Asserting initial state
        assertEquals(Thread.State.NEW, thread.getState());

        // Starting the thread and checking the state
        thread.start();
        assertEquals(Thread.State.RUNNABLE, thread.getState());

        // Waiting for the thread to enter TIMED_WAITING state
        Thread.sleep(100);
        assertEquals(Thread.State.TIMED_WAITING, thread.getState());

        // Waiting for the thread to finish
        thread.join();
        assertEquals(Thread.State.TERMINATED, thread.getState());
    }
}
