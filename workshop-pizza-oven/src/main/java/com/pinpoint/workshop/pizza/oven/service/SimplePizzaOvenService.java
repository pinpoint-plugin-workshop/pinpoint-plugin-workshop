package com.pinpoint.workshop.pizza.oven.service;

import com.pinpoint.workshop.pizza.oven.domain.PizzaOrder;
import com.pinpoint.workshop.pizza.oven.mapper.OrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author HyunGil Jeong
 */
@Service
public class SimplePizzaOvenService implements PizzaOvenService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private OrderMapper orderMapper;

    public SimplePizzaOvenService(OrderMapper orderMapper) {
        this.orderMapper = Objects.requireNonNull(orderMapper, "orderMapper must not be null");
    }

    @Override
    public boolean processOrder(UUID orderId, String pizzaName, int quantity) {
        PizzaOrder pizzaOrder = new PizzaOrder();
        pizzaOrder.setOrderId(orderId);
        pizzaOrder.setPizzaName(pizzaName);
        pizzaOrder.setOrderQuantity(quantity);
        try {
            boolean orderProcessed = orderMapper.insertPizzaOrder(pizzaOrder) == 1;
            Thread.sleep(ThreadLocalRandom.current().nextLong(1000));
            return orderProcessed;
        } catch (DuplicateKeyException e) {
            logger.error("Duplicate order : {}", orderId, e);
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Oven blew up", e);
            return false;
        }
    }
}
