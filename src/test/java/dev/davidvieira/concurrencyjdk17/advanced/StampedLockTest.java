package dev.davidvieira.concurrencyjdk17.advanced;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StampedLockTest {

    @Test
    @DisplayName("Using StampedLock for Optimistic Read and Write")
    void testStampedLock() throws InterruptedException {
        class StampedCounter {
            private final StampedLock lock = new StampedLock();
            private int count = 0;

            public void increment() {
                long stamp = lock.writeLock();
                try {
                    count++;
                } finally {
                    lock.unlockWrite(stamp);
                }
            }

            public int getCount() {
                long stamp = lock.tryOptimisticRead();
                int currentCount = count;
                if (!lock.validate(stamp)) {
                    stamp = lock.readLock();
                    try {
                        currentCount = count;
                    } finally {
                        lock.unlockRead(stamp);
                    }
                }
                return currentCount;
            }
        }

        StampedCounter counter = new StampedCounter();
        ExecutorService executor = Executors.newFixedThreadPool(3);

        Runnable writer = () -> {
            for (int i = 0; i < 10; i++) {
                counter.increment();
            }
        };

        Runnable reader = () -> {
            for (int i = 0; i < 10; i++) {
                counter.getCount();
            }
        };

        executor.submit(writer);
        executor.submit(reader);
        executor.submit(reader);

        executor.shutdown();
        assertTrue(executor.awaitTermination(1, TimeUnit.SECONDS));
    }
}
