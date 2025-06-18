package com.intershop.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class BuyRouterConfiguration {

    @Bean
    public RouterFunction<ServerResponse> buyHandlerRouter(BuyHandler buyHandler) {
        return RouterFunctions.route(RequestPredicates.POST("/buy"), buyHandler::buy);
    }
}
