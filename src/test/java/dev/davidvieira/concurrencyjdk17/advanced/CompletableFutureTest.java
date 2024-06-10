package dev.davidvieira.concurrencyjdk17.advanced;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CompletableFutureTest {

    @Test
    @DisplayName("Using CompletableFuture for Asynchronous Computation")
    void testCompletableFuture() throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return 42;
        });

        assertFalse(future.isDone());
        assertEquals(42, future.get());
        assertTrue(future.isDone());
    }

    @Test
    @DisplayName("Chaining CompletableFutures")
    void testCompletableFutureChaining() throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 2)
            .thenApplyAsync(result -> result * 3)
            .thenApplyAsync(result -> result + 5);

        assertEquals(11, future.get());
    }

    @Test
    @DisplayName("Handling Exceptions in CompletableFuture")
    void testCompletableFutureExceptionHandling() throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("Calculation failed!");
        });

        future.handle((result, exception) -> {
            if (exception != null) {
                return 0;
            }
            return result;
        });

        assertEquals(0, future.get());
    }
}
