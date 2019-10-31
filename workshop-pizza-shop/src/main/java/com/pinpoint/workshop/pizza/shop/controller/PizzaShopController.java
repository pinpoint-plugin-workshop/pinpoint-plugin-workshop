package com.pinpoint.workshop.pizza.shop.controller;

import com.pinpoint.workshop.pizza.shop.service.PizzaOrderService;
import com.pinpoint.workshop.pizza.shop.vo.CompletedPizzaOrder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author HyunGil Jeong
 */
@RestController
public class PizzaShopController {

    private final PizzaOrderService pizzaOrderService;

    public PizzaShopController(PizzaOrderService pizzaOrderService) {
        this.pizzaOrderService = Objects.requireNonNull(pizzaOrderService, "pizzaOrderService must not be null");
    }

    @GetMapping("/orderPizza")
    public CompletedPizzaOrder orderPizza(@RequestParam("customerName") String customerName,
                                          @RequestParam(value = "pizzaName", defaultValue = "potatoPizza") String pizzaName,
                                          @RequestParam("count") int count) {
        if (StringUtils.isEmpty(customerName)) {
            throw new IllegalArgumentException("customerName must not be empty");
        }
        if (StringUtils.isEmpty(pizzaName)) {
            throw new IllegalArgumentException("pizzaName must not be empty");
        }


        if (count < 1) {
            throw new IllegalArgumentException("count must be greater than 0");
        }
        if (count > 1000) {
            throw new IllegalArgumentException("you can only order up to 1000 pizza");
        }
        return pizzaOrderService.orderPizza(customerName, pizzaName, count);
    }
}
