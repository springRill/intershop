package com.intershop.configuration;

import com.intershop.controller.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfiguration {

    @Bean
    public RouterFunction<ServerResponse> buyHandlerRouter(BuyHandler buyHandler) {
        return RouterFunctions.route(RequestPredicates.POST("/buy"), buyHandler::buy);
    }

    @Bean
    public RouterFunction<ServerResponse> cartHandlerRouter(CartHandler cartHandler) {
        return RouterFunctions.route()
                .path("/cart", builder -> builder
                        .GET("/items",  cartHandler::getCartItems)
                        .POST("/items/{id}",  cartHandler::changeCartItem)
                ).build();
    }

    @Bean
    public RouterFunction<ServerResponse> defaultHandlerRouter(DefaultHandler defaultHandler) {
        return RouterFunctions.route(RequestPredicates.GET("/"), defaultHandler::homePage);
    }

    @Bean
    public RouterFunction<ServerResponse> imageRouter(ImageHandler imageHandler) {
        return RouterFunctions.route()
                .GET("/images/{imageName}", imageHandler::getImage)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> itemHandlerRouter(ItemHandler itemHandler) {
        return RouterFunctions.route()
                .path("/items", builder -> builder
                        .GET("/{id}",  itemHandler::getItems)
                        .POST("/{id}",  itemHandler::changeCartItem)
                ).build();
    }

    @Bean
    public RouterFunction<ServerResponse> mainHandlerRouter(MainHandler mainHandler) {
        return RouterFunctions.route()
                .path("/main", builder -> builder
                        .GET("/items",  mainHandler::getItems)
                        .POST("/items/{id}",  mainHandler::changeCartItem)
                ).build();
    }

    @Bean
    public RouterFunction<ServerResponse> ordersHandlerRouter(OrdersHandler ordersHandler) {
        return RouterFunctions.route()
                .path("/orders", builder -> builder
                        .GET("",  ordersHandler::getOrders)
                        .GET("/{id}",  ordersHandler::getOrder)
                ).build();
    }

}
