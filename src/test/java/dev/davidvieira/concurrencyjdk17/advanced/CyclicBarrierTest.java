package dev.davidvieira.concurrencyjdk17.advanced;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CyclicBarrierTest {

    @Test
    @DisplayName("Using CyclicBarrier to Synchronize Threads")
    void testCyclicBarrier() throws InterruptedException {
        CyclicBarrier barrier = new CyclicBarrier(3, () -> System.out.println("All threads reached the barrier"));

        Runnable task = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " waiting at barrier");
                barrier.await();
                System.out.println(Thread.currentThread().getName() + " passed the barrier");
            } catch (InterruptedException | BrokenBarrierException e) {
                Thread.currentThread().interrupt();
            }
        };

        ExecutorService executor = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            executor.submit(task);
        }

        executor.shutdown();
        assertTrue(executor.awaitTermination(1, TimeUnit.SECONDS));
    }
}
