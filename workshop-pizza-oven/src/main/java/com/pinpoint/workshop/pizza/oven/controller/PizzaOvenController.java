package com.pinpoint.workshop.pizza.oven.controller;

import com.pinpoint.workshop.pizza.oven.service.PizzaOvenService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.UUID;

/**
 * @author HyunGil Jeong
 */
@RestController
public class PizzaOvenController {

    private PizzaOvenService pizzaOvenService;

    public PizzaOvenController(PizzaOvenService pizzaOvenService) {
        this.pizzaOvenService = Objects.requireNonNull(pizzaOvenService, "pizzaOvenService must not be null");
    }

    @GetMapping("/cook")
    public boolean cookPizza(@RequestParam("orderId") String orderIdString,
                             @RequestParam("customerName") String customerName,
                             @RequestParam("orderQuantity") int orderQuantity) {
        if (StringUtils.isEmpty(orderIdString)) {
            throw new IllegalArgumentException("orderId must not be empty");
        }
        if (StringUtils.isEmpty(customerName)) {
            throw new IllegalArgumentException("customerName must not be empty");
        }
        if (orderQuantity < 1) {
            throw new IllegalArgumentException("orderQuantity must be greater than 0");
        }
        UUID orderId = UUID.fromString(orderIdString);
        return pizzaOvenService.processOrder(orderId, customerName, orderQuantity);
    }
}
