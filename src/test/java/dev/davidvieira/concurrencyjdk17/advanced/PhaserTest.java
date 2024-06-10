package dev.davidvieira.concurrencyjdk17.advanced;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PhaserTest {

    @Test
    @DisplayName("Using Phaser for Multiple Phase Synchronization")
    void testPhaser() throws InterruptedException {
        class Task implements Runnable {
            private final Phaser phaser;

            Task(Phaser phaser) {
                this.phaser = phaser;
            }

            @Override
            public void run() {
                phaser.arriveAndAwaitAdvance(); // Phase 1
                phaser.arriveAndAwaitAdvance(); // Phase 2
            }
        }

        Phaser phaser = new Phaser(1);
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 3; i++) {
            phaser.register();
            executor.submit(new Task(phaser));
        }

        phaser.arriveAndDeregister(); // Deregister main thread

        executor.shutdown();
        assertTrue(executor.awaitTermination(1, TimeUnit.SECONDS));
    }
}
