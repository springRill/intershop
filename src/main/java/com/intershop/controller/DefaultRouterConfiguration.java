package com.intershop.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class DefaultRouterConfiguration {

    @Bean
    public RouterFunction<ServerResponse> defaultHandlerRouter(DefaultHandler defaultHandler) {
        return RouterFunctions.route(RequestPredicates.GET("/"), defaultHandler::homePage);
    }

}
