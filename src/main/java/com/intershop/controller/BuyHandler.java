package com.intershop.controller;

import com.intershop.service.BuyService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class BuyHandler {

    BuyService buyService;

    public BuyHandler(BuyService buyService) {
        this.buyService = buyService;
    }

    public Mono<ServerResponse> buy(ServerRequest request) {
        return buyService.buyCart()
                .flatMap(orderId ->
                        ServerResponse
                                .seeOther(URI.create("/orders/%d?newOrder=true".formatted(orderId)))
                                .build()
                );
    }

}
