package com.pinpoint.workshop.pizza.shop.config;

import com.pinpoint.workshop.pizza.counter.PizzaCounter;
import com.pinpoint.workshop.pizza.counter.RandomWaitPizzaCounter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author HyunGil Jeong
 */
@Configuration
public class AppConfig {

    @Bean
    public PizzaCounter pizzaCounter() {
        return new RandomWaitPizzaCounter();
    }
}
