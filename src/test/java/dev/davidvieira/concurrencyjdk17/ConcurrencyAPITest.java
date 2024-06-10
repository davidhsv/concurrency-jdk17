package dev.davidvieira.concurrencyjdk17;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.*;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ConcurrencyAPITest {

    @Test
    @DisplayName("Using ExecutorService to Execute Tasks")
    void testExecutorService() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<String> future = executorService.submit(() -> "Task Completed");

        assertEquals("Task Completed", future.get());
        executorService.shutdown();
    }

    @Test
    @DisplayName("Using ScheduledExecutorService")
    void testScheduledExecutorService() throws InterruptedException {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        CountDownLatch latch = new CountDownLatch(1);

        scheduledExecutorService.schedule(() -> {
            System.out.println("Task Executed");
            latch.countDown();
        }, 1, TimeUnit.SECONDS);

        latch.await();
        scheduledExecutorService.shutdown();

        assertTrue(true); // Just to assert that the test completes
    }

    @Test
    @DisplayName("Using ForkJoinPool for Parallelism")
    void testUsingForkJoinPoolForParallelism() {
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        ForkJoinTask<Integer> task = forkJoinPool.submit(() -> {
            return IntStream.range(1, 1000).parallel().sum();
        });

        try {
            assertEquals(499500, task.get());
        } catch (InterruptedException | ExecutionException e) {
            fail(e);
        }
    }
}
