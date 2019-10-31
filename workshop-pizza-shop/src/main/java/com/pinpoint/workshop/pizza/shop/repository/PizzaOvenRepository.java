package com.pinpoint.workshop.pizza.shop.repository;

import java.util.UUID;

/**
 * @author HyunGil Jeong
 */
public interface PizzaOvenRepository {

    boolean placeOrder(UUID orderId, String pizzaName, int quantity);
}
