package com.pinpoint.workshop.pizza.shop.service;

import com.pinpoint.workshop.pizza.shop.vo.CompletedPizzaOrder;

/**
 * @author HyunGil Jeong
 */
public interface PizzaOrderService {

    CompletedPizzaOrder orderPizza(String customerName, int orderQuantity);
}
