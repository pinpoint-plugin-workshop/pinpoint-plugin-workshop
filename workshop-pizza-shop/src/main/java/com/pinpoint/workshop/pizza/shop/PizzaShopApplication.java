package com.pinpoint.workshop.pizza.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author HyunGil Jeong
 */
@SpringBootApplication
public class PizzaShopApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(PizzaShopApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(PizzaShopApplication.class, args);
    }
}
