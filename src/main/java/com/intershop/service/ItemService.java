package com.intershop.service;

import com.intershop.domain.Cart;
import com.intershop.domain.Item;
import com.intershop.dto.ItemDto;
import com.intershop.dto.ItemPageDto;
import com.intershop.dto.PagingDto;
import com.intershop.mapper.ItemMapper;
import com.intershop.repository.CartRepository;
import com.intershop.repository.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    ItemRepository itemRepository;

    CartRepository cartRepository;

    public ItemService(ItemRepository itemRepository, CartRepository cartRepository) {
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
    }

    public ItemPageDto findByTitle(String search, Pageable pageable) {
        Page<Item> itemListPage = itemRepository.findByTitleContaining(search, pageable);

        PagingDto pagingDto = new PagingDto();
        pagingDto.setPageSize(itemListPage.getSize());
        pagingDto.setHasPrevious(itemListPage.hasPrevious());
        pagingDto.setHasNext(itemListPage.hasNext());
        pagingDto.setPageNumber(itemListPage.getNumber() + 1);

        List<ItemDto> itemDtoList = itemListPage.stream().map(ItemMapper::toItemDto).toList();
        itemDtoList.forEach(itemDto -> {
            List<Cart> cartList = cartRepository.findByItemIdAndOrderIdIsNull(itemDto.getId());
            if (!cartList.isEmpty()) {
                itemDto.setCount(cartList.getFirst().getCount());
            } else {
                itemDto.setCount(0);
            }
        });

        ItemPageDto itemPageDto = new ItemPageDto();
        itemPageDto.setItemDtoList(itemDtoList);
        itemPageDto.setPagingDto(pagingDto);

        return itemPageDto;
    }

    public List<ItemDto> getCartItems() {
        return cartRepository.findByOrderIdIsNull().stream().map(cart -> {
            ItemDto itemDto = itemRepository.findById(cart.getItemId()).map(ItemMapper::toItemDto).orElseThrow();
            itemDto.setCount(cart.getCount());
            return itemDto;
        }).toList();
    }

    public ItemDto findByItemId(Long id) {
        ItemDto itemDto = itemRepository.findById(id).map(ItemMapper::toItemDto).orElseThrow();
        List<Cart> cartList = cartRepository.findByItemIdAndOrderIdIsNull(itemDto.getId());
        if (!cartList.isEmpty()) {
            itemDto.setCount(cartList.getFirst().getCount());
        } else {
            itemDto.setCount(0);
        }
        return itemDto;
    }
}
