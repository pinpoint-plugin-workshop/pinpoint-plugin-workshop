package com.pinpoint.workshop.pizza.shop.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;
import java.util.UUID;

/**
 * @author HyunGil Jeong
 */
@Repository
public class HttpPizzaOvenRepository implements PizzaOvenRepository {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RestTemplate restTemplate;
    private final String ovenHost;
    private final int ovenPort;

    public HttpPizzaOvenRepository(RestTemplate restTemplate,
                                   @Value("${pizza.oven.host}") String ovenHost,
                                   @Value("${pizza.oven.port}") int ovenPort) {
        this.restTemplate = Objects.requireNonNull(restTemplate, "restTemplate must not be null");
        this.ovenHost = Objects.requireNonNull(ovenHost, "ovenHost must not be null");
        this.ovenPort = ovenPort;
    }

    @Override
    public boolean placeOrder(UUID orderId, String customerName, int quantity) {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(ovenHost)
                .port(ovenPort)
                .path("/cook")
                .queryParam("orderId", orderId.toString())
                .queryParam("customerName", customerName)
                .queryParam("orderQuantity", quantity)
                .build();
        URI uri = uriComponents.toUri();
        try {
            return restTemplate.getForObject(uri, Boolean.class);
        } catch (RestClientException e) {
            logger.error("Failed to call pizza oven via {}", uri.toString(), e);
            return false;
        }
    }
}
