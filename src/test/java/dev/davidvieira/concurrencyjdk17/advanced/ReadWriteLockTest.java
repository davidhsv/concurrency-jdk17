package dev.davidvieira.concurrencyjdk17.advanced;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ReadWriteLockTest {

    @Test
    @DisplayName("Using ReadWriteLock for Multiple Readers and Single Writer")
    void testReadWriteLock() throws InterruptedException {
        class ReadWriteCounter {
            private final ReadWriteLock lock = new ReentrantReadWriteLock();
            private final Lock readLock = lock.readLock();
            private final Lock writeLock = lock.writeLock();
            private int count = 0;

            public void increment() {
                writeLock.lock();
                try {
                    count++;
                } finally {
                    writeLock.unlock();
                }
            }

            public int getCount() {
                readLock.lock();
                try {
                    return count;
                } finally {
                    readLock.unlock();
                }
            }
        }

        ReadWriteCounter counter = new ReadWriteCounter();
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
