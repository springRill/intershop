package com.intershop.controller;

import com.intershop.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class OrdersHandler {

    private final OrderService orderService;

    public OrdersHandler(OrderService orderService) {
        this.orderService = orderService;
    }

    @PreAuthorize("isAuthenticated()")
    public Mono<ServerResponse> getOrders(ServerRequest request) {
        return orderService.getOrders()
                .collectList()
                .flatMap(orders -> ViewRenderer.render("orders", Map.of("orders", orders)));
    }

    @PreAuthorize("isAuthenticated()")
    public Mono<ServerResponse> getOrder(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        boolean newOrder = Boolean.parseBoolean(request.queryParam("newOrder").orElse("false"));

        return orderService.getOrder(id)
                .flatMap(order -> ViewRenderer.render("order", Map.of(
                        "order", order,
                        "newOrder", newOrder
                )));
    }

}
