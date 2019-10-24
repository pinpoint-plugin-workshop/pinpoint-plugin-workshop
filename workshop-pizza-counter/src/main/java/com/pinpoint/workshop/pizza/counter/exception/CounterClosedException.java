package com.pinpoint.workshop.pizza.counter.exception;

/**
 * @author HyunGil Jeong
 */
public class CounterClosedException extends Exception {

    public CounterClosedException(String customerName) {
        this(customerName, null);
    }

    public CounterClosedException(String customerName, Throwable cause) {
        super("Counter closed. Sorry " + customerName, cause);
    }
}
