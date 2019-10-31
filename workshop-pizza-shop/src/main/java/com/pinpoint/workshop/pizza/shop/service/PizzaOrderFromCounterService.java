package com.pinpoint.workshop.pizza.shop.service;

import com.pinpoint.workshop.pizza.counter.PizzaCounter;
import com.pinpoint.workshop.pizza.counter.exception.CounterClosedException;
import com.pinpoint.workshop.pizza.shop.repository.PizzaOvenRepository;
import com.pinpoint.workshop.pizza.shop.vo.CompletedPizzaOrder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

/**
 * @author HyunGil Jeong
 */
@Service
public class PizzaOrderFromCounterService implements PizzaOrderService {

    private final PizzaCounter pizzaCounter;
    private final PizzaOvenRepository pizzaOvenRepository;

    public PizzaOrderFromCounterService(PizzaCounter pizzaCounter, PizzaOvenRepository pizzaOvenRepository) {
        this.pizzaCounter = Objects.requireNonNull(pizzaCounter, "pizzaCounter must not be null");
        this.pizzaOvenRepository = Objects.requireNonNull(pizzaOvenRepository, "pizzaOvenRepository must not be null");
    }

    @Override
    public CompletedPizzaOrder orderPizza(String customerName, String pizzaName, int orderQuantity) {
        try {
            UUID orderId = pizzaCounter.waitInLine(customerName, pizzaName);
            boolean orderSuccessful = pizzaOvenRepository.placeOrder(orderId, customerName, orderQuantity);
            return new CompletedPizzaOrder(orderId.toString(), customerName, pizzaName, orderQuantity, orderSuccessful);
        } catch (CounterClosedException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
