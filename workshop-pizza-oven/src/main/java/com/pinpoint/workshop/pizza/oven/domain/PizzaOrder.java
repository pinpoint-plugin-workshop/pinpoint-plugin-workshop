package com.pinpoint.workshop.pizza.oven.domain;

import java.util.UUID;

/**
 * @author HyunGil Jeong
 */
public class PizzaOrder {

    private UUID orderId;
    private String customerName;
    private Integer orderQuantity;

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(Integer orderQuantity) {
        this.orderQuantity = orderQuantity;
    }
}
