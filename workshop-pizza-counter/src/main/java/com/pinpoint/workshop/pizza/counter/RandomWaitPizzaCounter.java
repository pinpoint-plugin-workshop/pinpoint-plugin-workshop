package com.pinpoint.workshop.pizza.counter;

import com.pinpoint.workshop.pizza.counter.exception.CounterClosedException;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author HyunGil Jeong
 */
public class RandomWaitPizzaCounter implements PizzaCounter {

    private static final long MIN_WAIT_TIME_MS = 100;
    private static final long MAX_WAIT_TIME_MS = 3000;

    public UUID waitInLine(String customerName) throws CounterClosedException {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        long waitTimeMs = random.nextLong(MAX_WAIT_TIME_MS - MIN_WAIT_TIME_MS) + MIN_WAIT_TIME_MS;
        try {
            Thread.sleep(waitTimeMs);
        } catch (InterruptedException e) {
            // counter closed!
            Thread.currentThread().interrupt();
            throw new CounterClosedException(customerName);
        }
        return UUID.randomUUID();
    }
}
