package com.intershop.controller;

import com.intershop.dto.ItemActionEnum;
import com.intershop.dto.ItemDto;
import com.intershop.dto.ItemSortEnum;
import com.intershop.service.CartService;
import com.intershop.service.ItemService;
import com.intershop.utils.AuthUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class MainHandler {

    private final ItemService itemService;

    private final CartService cartService;

    public MainHandler(ItemService itemService, CartService cartService) {
        this.itemService = itemService;
        this.cartService = cartService;
    }

    public Mono<ServerResponse> getItems(ServerRequest request) {//,
        String search = request.queryParam("search").map(String::valueOf).orElse("");
        ItemSortEnum sort = request.queryParam("sort").map(ItemSortEnum::valueOf).orElse(ItemSortEnum.NO);
        Integer pageSize = request.queryParam("pageSize").map(Integer::valueOf).orElse(10);
        Integer pageNumber = request.queryParam("pageNumber").map(Integer::valueOf).orElse(1);


        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(sort.getSortColumnName()).ascending());
        return AuthUtils.getCurrentUserId(request)
                .flatMap(userId -> itemService.findByTitle(userId, search, pageable))
                .flatMap(itemPageDto -> {
                    int colCount = 2;
                    List<List<ItemDto>> itemsByRows = new ArrayList<>();
                    List<ItemDto> itemDtoRow = new ArrayList<>();
                    itemsByRows.add(itemDtoRow);

                    for (ItemDto itemDto : itemPageDto.getItemDtoList()) {
                        itemDtoRow.add(itemDto);
                        if (itemDtoRow.size() >= colCount) {
                            itemDtoRow = new ArrayList<>();
                            itemsByRows.add(itemDtoRow);
                        }
                    }

                    return AuthUtils.render("main", Map.of(
                            "items", itemsByRows,
                            "search", search,
                            "sort", sort.name(),
                            "paging", itemPageDto.getPagingDto()
                    ));
                });

    }

    @PreAuthorize("isAuthenticated()")
    public Mono<ServerResponse> changeCartItem(ServerRequest request) {
        Long itemId = Long.valueOf(request.pathVariable("id"));
        return AuthUtils.getCurrentUserId(request)
                .flatMap(userId -> {
                    return request.formData()
                            .map(data -> data.getFirst("action"))
                            .map(ItemActionEnum::valueOf)
                            .flatMap(action -> cartService.changeCartItem(itemId, action, userId)); // <-- передаём userId
                })
                .then(ServerResponse.seeOther(URI.create("/main/items")).build());
    }

}
