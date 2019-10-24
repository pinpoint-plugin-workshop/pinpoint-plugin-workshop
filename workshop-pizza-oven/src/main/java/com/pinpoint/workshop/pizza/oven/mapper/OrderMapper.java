package com.pinpoint.workshop.pizza.oven.mapper;

import com.pinpoint.workshop.pizza.oven.domain.PizzaOrder;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author HyunGil Jeong
 */
@Mapper
@Component
public interface OrderMapper {

    int insertPizzaOrder(PizzaOrder pizzaOrder);
}
