package com.intershop.controller;

import com.intershop.service.OrderService;
import com.intershop.utils.RenderUtils;
import com.intershop.utils.UserUtils;
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
        return UserUtils.getCurrentUserId(request)
                .flatMap(userId -> {
                    return orderService.getOrders(userId)
                            .collectList()
                            .flatMap(orders -> RenderUtils.render("orders", Map.of("orders", orders)));
                });
    }

    @PreAuthorize("isAuthenticated()")
    public Mono<ServerResponse> getOrder(ServerRequest request) {
        Long orgerId = Long.valueOf(request.pathVariable("id"));
        boolean newOrder = Boolean.parseBoolean(request.queryParam("newOrder").orElse("false"));

        return UserUtils.getCurrentUserId(request)
                .flatMap(userId -> {
                    return orderService.getOrder(orgerId, userId)
                            .flatMap(order -> RenderUtils.render("order", Map.of(
                                    "order", order,
                                    "newOrder", newOrder
                            )));
                });
    }

}
