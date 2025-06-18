package com.intershop.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class MainRouterConfiguration {

    @Bean
    public RouterFunction<ServerResponse> mainHandlerRouter(MainHandler mainHandler) {
        return RouterFunctions.route()
                .path("/main", builder -> builder
                        .GET("/items",  mainHandler::getItems)
                        .POST("/items/{id}",  mainHandler::changeCartItem)
                ).build();
    }

}
