package dev.davidvieira.concurrencyjdk17.advanced;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ThreadLocalTest {

    @Test
    @DisplayName("Using ThreadLocal Variables")
    void testThreadLocal() throws InterruptedException {
        ThreadLocal<Integer> threadLocalValue = ThreadLocal.withInitial(() -> 1);

        Runnable task1 = () -> threadLocalValue.set(threadLocalValue.get() + 1);
        Runnable task2 = () -> threadLocalValue.set(threadLocalValue.get() + 2);

        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        assertEquals(1, (int) threadLocalValue.get());
    }
}
