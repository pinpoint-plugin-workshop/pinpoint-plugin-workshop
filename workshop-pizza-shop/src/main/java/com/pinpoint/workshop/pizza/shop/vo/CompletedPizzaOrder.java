package com.pinpoint.workshop.pizza.shop.vo;

/**
 * @author HyunGil Jeong
 */
public class CompletedPizzaOrder {

    private final String orderId;
    private final String customerName;
    private final int pizzaQuantity;
    private final boolean orderSuccessful;

    public CompletedPizzaOrder(String orderId, String customerName, int pizzaQuantity, boolean orderSuccessful) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.pizzaQuantity = pizzaQuantity;
        this.orderSuccessful = orderSuccessful;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getPizzaQuantity() {
        return pizzaQuantity;
    }

    public boolean isOrderSuccessful() {
        return orderSuccessful;
    }
}
