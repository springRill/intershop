package com.intershop.controller;

import com.intershop.service.BuyService;
import com.intershop.service.ItemService;
import com.intershop.service.PaymentApiService;
import com.intershop.utils.UserUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class BuyHandler {

    private final BuyService buyService;

    private final ItemService itemService;

    private final PaymentApiService paymentApiService;

    public BuyHandler(BuyService buyService, ItemService itemService, PaymentApiService paymentApiService) {
        this.buyService = buyService;
        this.itemService = itemService;
        this.paymentApiService = paymentApiService;
    }

    @PreAuthorize("isAuthenticated()")
    public Mono<ServerResponse> buy(ServerRequest request) {
        return UserUtils.getCurrentUserId(request).flatMap(userId -> {
            return itemService.getCartItemsByUserId(userId)
                    .collectList()
                    .flatMap(itemDtoList -> {
                        double total = itemDtoList.stream()
                                .mapToDouble(item -> item.getCount() * item.getPrice())
                                .sum();

                        return paymentApiService.pay(total, userId)
                                .then(buyService.buyCart(userId))
                                .flatMap(orderId -> {
                                    return ServerResponse.seeOther(URI.create("/orders/%d?newOrder=true".formatted(orderId)))
                                            .build();
                                })
                                .onErrorResume(WebClientResponseException.class, e -> {
                                    return ServerResponse.seeOther(URI.create("/cart/items?paymentError=true"))
                                            .build();
                                });
                    });
        });
    }
}
