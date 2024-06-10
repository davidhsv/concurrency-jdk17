package dev.davidvieira.concurrencyjdk17.advanced;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ReentrantLockWithConditionVariablesTest {

    @Test
    @DisplayName("Using ReentrantLock with Condition Variables")
    void testReentrantLockWithConditionVariables() throws InterruptedException {
        class BoundedBuffer {
            private final Lock lock = new ReentrantLock();
            private final Condition notFull = lock.newCondition();
            private final Condition notEmpty = lock.newCondition();
            private final Object[] items = new Object[10];
            private int putPtr, takePtr, count;

            public void put(Object x) throws InterruptedException {
                lock.lock();
                try {
                    while (count == items.length)
                        notFull.await();
                    items[putPtr] = x;
                    if (++putPtr == items.length) putPtr = 0;
                    count++;
                    notEmpty.signal();
                } finally {
                    lock.unlock();
                }
            }

            public Object take() throws InterruptedException {
                lock.lock();
                try {
                    while (count == 0)
                        notEmpty.await();
                    Object x = items[takePtr];
                    if (++takePtr == items.length) takePtr = 0;
                    count--;
                    notFull.signal();
                    return x;
                } finally {
                    lock.unlock();
                }
            }
        }

        BoundedBuffer buffer = new BoundedBuffer();
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Runnable producer = () -> {
            try {
                for (int i = 0; i < 5; i++) {
                    buffer.put(i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Runnable consumer = () -> {
            try {
                for (int i = 0; i < 5; i++) {
                    buffer.take();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        executor.submit(producer);
        executor.submit(consumer);

        executor.shutdown();
        assertTrue(executor.awaitTermination(1, TimeUnit.SECONDS));
    }
}
