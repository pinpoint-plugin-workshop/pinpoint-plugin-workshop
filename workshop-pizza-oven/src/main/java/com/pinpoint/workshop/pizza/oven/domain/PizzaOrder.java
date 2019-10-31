package com.pinpoint.workshop.pizza.oven.domain;

import java.util.UUID;

/**
 * @author HyunGil Jeong
 */
public class PizzaOrder {

    private UUID orderId;
    private String pizzaName;
    private Integer orderQuantity;

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public String getPizzaName() {
        return pizzaName;
    }

    public void setPizzaName(String pizzaName) {
        this.pizzaName = pizzaName;
    }

    public Integer getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(Integer orderQuantity) {
        this.orderQuantity = orderQuantity;
    }
}
