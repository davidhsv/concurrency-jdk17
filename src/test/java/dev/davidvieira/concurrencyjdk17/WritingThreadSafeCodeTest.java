package dev.davidvieira.concurrencyjdk17;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class WritingThreadSafeCodeTest {

    @Test
    @DisplayName("Using Atomic Classes")
    void testAtomicClasses() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 1000; i++) {
            executorService.submit(atomicInteger::incrementAndGet);
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertEquals(1000, atomicInteger.get());
    }

    @Test
    @DisplayName("Using Synchronized Blocks")
    void testSynchronizedBlocks() throws InterruptedException {
        class Counter {
            private int count = 0;

            public synchronized void increment() {
                count++;
            }

            public int getCount() {
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

        assertEquals(1000, counter.getCount());
    }

    @Test
    @DisplayName("Using Locks")
    void testUsingLocks() throws InterruptedException {
        class Counter {
            private int count = 0;
            private final Lock lock = new ReentrantLock();

            public void increment() {
                lock.lock();
                try {
                    count++;
                } finally {
                    lock.unlock();
                }
            }

            public int getCount() {
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

        assertEquals(1000, counter.getCount());
    }

    @Test
    @DisplayName("Using ConcurrentHashMap")
    void testUsingConcurrentHashMap() throws InterruptedException {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 1000; i++) {
            executorService.submit(() -> map.merge("key", 1, Integer::sum));
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);

        assertEquals(1000, map.get("key"));
    }
}
