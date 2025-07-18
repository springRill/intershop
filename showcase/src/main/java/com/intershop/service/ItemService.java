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

    @Cacheable(value = "itemPages", key = "#userId + '_' + #search + '_' + #pageable")
    public Mono<ItemPageDto> findByTitle(Long userId, String search, Pageable pageable) {
        Mono<Long> totalCountMono = itemRepository.countByTitleContaining(search);

        Flux<ItemDto> itemDtoFlux = itemRepository.findByTitleContaining(search, pageable)
                .concatMap(item -> {
                    ItemDto dto = ItemMapper.toItemDto(item);
                    dto.setCount(0);
                    return cartRepository.findByItemIdAndUserIdAndOrderIdIsNull(item.getId(), userId)
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

    @Cacheable(value = "carts", key = "#userId")
    public Flux<ItemDto> getCartItemsByUserId(Long userId) {
        return cartRepository.findByUserIdAndOrderIdIsNull(userId, Sort.by(Sort.Direction.ASC, "id"))
                .concatMap(cart ->
                        itemRepository.findById(cart.getItemId())
                                .map(ItemMapper::toItemDto)
                                .map(dto -> {
                                    dto.setCount(cart.getCount());
                                    return dto;
                                })
                );
    }

    @Cacheable(value = "items", key = "#itemId + '_' + #userId")
    public Mono<ItemDto> findByItemIdAndUserId(Long itemId, Long userId) {
        return itemRepository.findById(itemId)
                .switchIfEmpty(Mono.error(new RuntimeException("Item not found")))
                .flatMap(item ->
                        cartRepository.findByItemIdAndUserIdAndOrderIdIsNull(item.getId(), userId)
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
