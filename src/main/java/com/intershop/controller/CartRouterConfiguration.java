package com.intershop.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class CartRouterConfiguration {

    @Bean
    public RouterFunction<ServerResponse> cartHandlerRouter(CartHandler cartHandler) {
        return RouterFunctions.route()
                .path("/cart", builder -> builder
                        .GET("/items",  cartHandler::getCartItems)
                        .POST("/items/{id}",  cartHandler::changeCartItem)
                ).build();
    }

}
