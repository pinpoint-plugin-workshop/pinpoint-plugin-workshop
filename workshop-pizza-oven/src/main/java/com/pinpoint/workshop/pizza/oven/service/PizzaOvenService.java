package com.pinpoint.workshop.pizza.oven.service;

import java.util.UUID;

/**
 * @author HyunGil Jeong
 */
public interface PizzaOvenService {

    boolean processOrder(UUID orderId, String pizzaName, int quantity);
}
