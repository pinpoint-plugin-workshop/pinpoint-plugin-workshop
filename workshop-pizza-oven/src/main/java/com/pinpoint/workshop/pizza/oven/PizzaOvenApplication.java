package com.pinpoint.workshop.pizza.oven;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author HyunGil Jeong
 */
@SpringBootApplication
public class PizzaOvenApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(PizzaOvenApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(PizzaOvenApplication.class, args);
    }
}
