package com.intershop.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class OrdersRouterConfiguration {

    @Bean
    public RouterFunction<ServerResponse> ordersHandlerRouter(OrdersHandler ordersHandler) {
        return RouterFunctions.route()
                .path("/orders", builder -> builder
                        .GET("",  ordersHandler::getOrders)
                        .GET("/{id}",  ordersHandler::getOrder)
                ).build();
    }
}
