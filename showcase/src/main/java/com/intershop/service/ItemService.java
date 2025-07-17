package com.intershop.service;

import com.intershop.domain.Cart;
import com.intershop.dto.ItemDto;
import com.intershop.dto.ItemPageDto;
import com.intershop.dto.PagingDto;
import com.intershop.mapper.ItemMapper;
import com.intershop.repository.CartRepository;
import com.intershop.repository.ItemRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    private final CartRepository cartRepository;

    public ItemService(ItemRepository itemRepository, CartRepository cartRepository) {
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
    }

    @Cacheable(value = "itemPages", key = "#search + '_' + #pageable")
    public Mono<ItemPageDto> findByTitle(String search, Pageable pageable) {
        Mono<Long> totalCountMono = itemRepository.countByTitleContaining(search);

        Flux<ItemDto> itemDtoFlux = itemRepository.findByTitleContaining(search, pageable)
                .concatMap(item -> {
                    ItemDto dto = ItemMapper.toItemDto(item);
                    dto.setCount(0);
                    return cartRepository.findByItemIdAndOrderIdIsNull(item.getId())
                            .next()
                            .map(cart -> {
                                dto.setCount(cart.getCount());
                                return dto;
                            })
                            .defaultIfEmpty(dto);
                });

        Mono<List<ItemDto>> itemDtoListMono = itemDtoFlux
                .collectList();

        return Mono.zip(itemDtoListMono, totalCountMono)
                .map(tuple -> {
                    List<ItemDto> itemDtoList = tuple.getT1();
                    long totalCount = tuple.getT2();

                    PagingDto pagingDto = new PagingDto();
                    pagingDto.setPageSize(pageable.getPageSize());
                    pagingDto.setPageNumber(pageable.getPageNumber() + 1);
                    pagingDto.setHasPrevious(pageable.getPageNumber() > 0);
                    pagingDto.setHasNext((pageable.getPageNumber() + 1) * pageable.getPageSize() < totalCount);

                    ItemPageDto itemPageDto = new ItemPageDto();
                    itemPageDto.setItemDtoList(itemDtoList);
                    itemPageDto.setPagingDto(pagingDto);

                    return itemPageDto;
                });
    }

    @Cacheable(value = "carts", key = "'anonymousCart'")
    public Flux<ItemDto> getCartItems() {
        return cartRepository.findByOrderIdIsNull(Sort.by(Sort.Direction.ASC, "id"))
                .concatMap(cart ->
                        itemRepository.findById(cart.getItemId())
                                .map(ItemMapper::toItemDto)
                                .map(dto -> {
                                    dto.setCount(cart.getCount());
                                    return dto;
                                })
                );
    }

//    @Cacheable(value = "items", key = "#id")
    public Mono<ItemDto> findByItemId(Long id) {
        return itemRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Item not found")))
                .flatMap(item ->
                        cartRepository.findByItemIdAndOrderIdIsNull(item.getId())
                                .next()
                                .defaultIfEmpty(new Cart())
                                .map(cart -> {
                                    ItemDto dto = ItemMapper.toItemDto(item);
                                    dto.setCount(cart.getCount() != null ? cart.getCount() : 0);
                                    return dto;
                                })
                );
    }

}
