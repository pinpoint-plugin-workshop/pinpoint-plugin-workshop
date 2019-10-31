package com.pinpoint.workshop.pizza.shop.vo;

/**
 * @author HyunGil Jeong
 */
public class CompletedPizzaOrder {

    private final String orderId;
    private final String customerName;
    private final String pizzaName;
    private final int pizzaQuantity;
    private final boolean orderSuccessful;

    public CompletedPizzaOrder(String orderId, String customerName, String pizzaName, int pizzaQuantity, boolean orderSuccessful) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.pizzaName = pizzaName;
        this.pizzaQuantity = pizzaQuantity;
        this.orderSuccessful = orderSuccessful;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getPizzaName() {
        return pizzaName;
    }

    public int getPizzaQuantity() {
        return pizzaQuantity;
    }

    public boolean isOrderSuccessful() {
        return orderSuccessful;
    }
}
