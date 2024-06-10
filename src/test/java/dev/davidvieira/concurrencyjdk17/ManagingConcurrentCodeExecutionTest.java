package dev.davidvieira.concurrencyjdk17;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.*;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ManagingConcurrentCodeExecutionTest {

    @Test
    @DisplayName("Create Worker Threads Using Runnable")
    void testCreateWorkerThreadsUsingRunnable() throws InterruptedException, ExecutionException {
        // Creating a worker thread using Runnable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(() -> "Task Completed");

        // Waiting for the result
        assertEquals("Task Completed", future.get());

        // Shutting down the executor
        executorService.shutdown();
    }

    @Test
    @DisplayName("Develop Thread-Safe Code Using Synchronized")
    void testDevelopThreadSafeCode() throws InterruptedException {
        // Using a synchronized block to develop thread-safe code
        class Counter {
            private int count = 0;

            public synchronized void increment() {
                count++;
            }

            public synchronized int getCount() {
                return count;
            }
        }

        Counter counter = new Counter();
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 1000; i++) {
            executorService.submit(counter::increment);
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);

        // Asserting that the counter has reached 1000
        assertEquals(1000, counter.getCount());
    }

    @Test
    @DisplayName("Process Collections Concurrently Using Parallel Streams")
    void testProcessCollectionsConcurrentlyUsingParallelStreams() {
        // Processing a collection concurrently using parallel streams
        long count = IntStream.range(1, 1000).parallel().filter(i -> i % 2 == 0).count();

        // Asserting that there are 499 even numbers between 1 and 999
        assertEquals(499, count);
    }
}
