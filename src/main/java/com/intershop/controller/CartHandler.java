package com.intershop.controller;

import com.intershop.dto.ItemActionEnum;
import com.intershop.service.CartService;
import com.intershop.service.ItemService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.util.Map;

@Component
public class CartHandler {

    ItemService itemService;

    CartService cartService;

    public CartHandler(ItemService itemService, CartService cartService) {
        this.itemService = itemService;
        this.cartService = cartService;
    }

    public Mono<ServerResponse> getCartItems(ServerRequest request) {
        return itemService.getCartItems()
                .collectList()
                .flatMap(itemDtoList -> {
                    double total = itemDtoList.stream()
                            .mapToDouble(item -> item.getCount() * item.getPrice())
                            .sum();

                    return ServerResponse.ok().render("cart", Map.of(
                            "items", itemDtoList,
                            "total", BigDecimal.valueOf(total).setScale(2, RoundingMode.HALF_UP),
                            "empty", itemDtoList.isEmpty()
                    ));
                });
    }

    public Mono<ServerResponse> changeCartItem(ServerRequest request){
        Long id = Long.valueOf(request.pathVariable("id"));
        return request.formData()
                .map(data -> data.getFirst("action"))
                .map(ItemActionEnum::valueOf)
                .flatMap(action -> cartService.changeCartItem(id, action))
                .then(ServerResponse.seeOther(URI.create("/cart/items")).build());
    }

}
