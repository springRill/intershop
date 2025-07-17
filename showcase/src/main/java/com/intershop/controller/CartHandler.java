package com.intershop.controller;

import com.intershop.dto.ItemActionEnum;
import com.intershop.service.CartService;
import com.intershop.service.ItemService;
import com.intershop.service.PaymentApiService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

@Component
public class CartHandler {

    private final ItemService itemService;

    private final CartService cartService;

    private final PaymentApiService paymentApiService;

    public CartHandler(ItemService itemService, CartService cartService, PaymentApiService paymentApiService) {
        this.itemService = itemService;
        this.cartService = cartService;
        this.paymentApiService = paymentApiService;
    }

    public Mono<ServerResponse> getCartItems(ServerRequest request) {
        boolean paymentError = request.queryParam("paymentError")
                .map(Boolean::parseBoolean)
                .orElse(false);

        return itemService.getCartItems()
                .collectList()
                .flatMap(itemDtoList -> {
                    double total = itemDtoList.stream()
                            .mapToDouble(item -> item.getCount() * item.getPrice())
                            .sum();

                    return paymentApiService.getBalance()
                            .flatMap(balance -> {
                                return ViewRenderer.render("cart", Map.of(
                                        "items", itemDtoList,
                                        "total", total,
                                        "empty", itemDtoList.isEmpty(),
                                        "balance", balance,
                                        "paymentError", paymentError ? "Оплата заказа не прошла" : ""
                                ));
                            })
                            .onErrorResume(WebClientRequestException.class, ex -> {
                                return ViewRenderer.render("cart", Map.of(
                                        "items", itemDtoList,
                                        "total", total,
                                        "empty", itemDtoList.isEmpty(),
                                        "error", "Сервер платежей не доступен, попробуйте позже",
                                        "paymentError", paymentError ? "Оплата заказа не прошла" : ""
                                ));
                            });
                });

    }

    public Mono<ServerResponse> changeCartItem(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return request.formData()
                .map(data -> data.getFirst("action"))
                .map(ItemActionEnum::valueOf)
                .flatMap(action -> cartService.changeCartItem(id, action))
                .then(ServerResponse.seeOther(URI.create("/cart/items")).build());
    }

}
