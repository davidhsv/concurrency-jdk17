package dev.davidvieira.concurrencyjdk17.advanced;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ForkJoinFrameworkTest {

    @Test
    @DisplayName("Using ForkJoinPool with RecursiveTask")
    void testForkJoinPool() {
        class Fibonacci extends RecursiveTask<Integer> {
            final int n;

            Fibonacci(int n) {
                this.n = n;
            }

            @Override
            protected Integer compute() {
                if (n <= 1)
                    return n;
                Fibonacci f1 = new Fibonacci(n - 1);
                Fibonacci f2 = new Fibonacci(n - 2);
                f1.fork();
                return f2.compute() + f1.join();
            }
        }

        ForkJoinPool pool = new ForkJoinPool();
        Fibonacci task = new Fibonacci(10);
        int result = pool.invoke(task);
        assertEquals(55, result);
    }
}
