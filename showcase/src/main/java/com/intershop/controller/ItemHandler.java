package com.intershop.controller;

import com.intershop.dto.ItemActionEnum;
import com.intershop.service.CartService;
import com.intershop.service.ItemService;
import com.intershop.utils.RenderUtils;
import com.intershop.utils.UserUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

@Component
public class ItemHandler {

    private final ItemService itemService;

    private final CartService cartService;

    public ItemHandler(ItemService itemService, CartService cartService) {
        this.itemService = itemService;
        this.cartService = cartService;
    }

    public Mono<ServerResponse> getItems(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return UserUtils.getCurrentUserId(request)
                .flatMap(userId -> {
                    return itemService.findByItemIdAndUserId(id, userId).flatMap(itemDto -> {
                        return RenderUtils.render("item", Map.of(
                                "item", itemDto
                        ));
                    });
                });
    }

    @PreAuthorize("isAuthenticated()")
    public Mono<ServerResponse> changeCartItem(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return UserUtils.getCurrentUserId(request)
                .flatMap(userId -> {
                    return request.formData()
                            .map(data -> data.getFirst("action"))
                            .map(ItemActionEnum::valueOf)
                            .flatMap(action -> cartService.changeCartItem(id, action, userId));

                })
                .then(ServerResponse.seeOther(URI.create("/items/%d".formatted(id))).build());
    }

}
