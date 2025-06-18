package com.intershop.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ItemRouterConfiguration {

    @Bean
    public RouterFunction<ServerResponse> itenHandlerRouter(ItemHandler itemHandler) {
        return RouterFunctions.route()
                .path("/items", builder -> builder
                        .GET("/{id}",  itemHandler::getItems)
                        .POST("/{id}",  itemHandler::changeCartItem)
                ).build();
    }

}
