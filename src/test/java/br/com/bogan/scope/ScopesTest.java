package br.com.bogan.scope;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ScopesTest {

    @Test
    void singletonReturnsSameInstanceAndIsThreadSafe() throws Exception {
        var scope = new SingletonScope();
        var counter = new AtomicInteger();
        var pool = Executors.newFixedThreadPool(8);
        var latch = new CountDownLatch(20);
        Object[] holder = new Object[20];
        for (int i = 0; i < 20; i++) {
            final int idx = i;
            pool.submit(() -> {
                try {
                    holder[idx] = scope.getOrCreate("x", counter::incrementAndGet);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await(2, TimeUnit.SECONDS);
        pool.shutdownNow();
        // All should be the same instance (the first Integer 1)
        Object first = holder[0];
        for (Object o : holder) assertSame(first, o);
        assertEquals(1, counter.get());
    }

    @Test
    void prototypeAlwaysCreatesNew() {
        var scope = new PrototypeScope();
        Object a = scope.getOrCreate("x", Object::new);
        Object b = scope.getOrCreate("x", Object::new);
        assertNotSame(a, b);
    }
}
