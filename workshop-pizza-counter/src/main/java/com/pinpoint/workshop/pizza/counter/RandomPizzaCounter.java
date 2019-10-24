package com.pinpoint.workshop.pizza.counter;

import com.pinpoint.workshop.pizza.counter.exception.CounterClosedException;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author HyunGil Jeong
 */
public class RandomPizzaCounter implements PizzaCounter {

    private static final int MAX_WAIT_TIME_SECONDS = 5;

    public UUID waitInLine(String customerName) throws CounterClosedException {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int waitTime = random.nextInt(MAX_WAIT_TIME_SECONDS + 1);
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            // counter closed!
            Thread.currentThread().interrupt();
            throw new CounterClosedException(customerName);
        }
        return UUID.randomUUID();
    }
}
