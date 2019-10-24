package com.pinpoint.workshop.pizza.counter;

import com.pinpoint.workshop.pizza.counter.exception.CounterClosedException;

import java.util.UUID;

/**
 * @author HyunGil Jeong
 */
public interface PizzaCounter {

    UUID waitInLine(String customerName) throws CounterClosedException;
}
