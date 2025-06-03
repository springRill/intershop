package com.intershop.service;

import com.intershop.dto.ItemDto;
import com.intershop.dto.OrdersDto;
import com.intershop.mapper.ItemMapper;
import com.intershop.mapper.OrderMapper;
import com.intershop.repository.CartRepository;
import com.intershop.repository.ItemRepository;
import com.intershop.repository.OrdersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    OrdersRepository ordersRepository;

    CartRepository cartRepository;

    ItemRepository itemRepository;

    public OrderService(OrdersRepository ordersRepository, CartRepository cartRepository, ItemRepository itemRepository) {
        this.ordersRepository = ordersRepository;
        this.cartRepository = cartRepository;
        this.itemRepository = itemRepository;
    }

    public OrdersDto getOrder(Long id) {
        OrdersDto ordersDto = ordersRepository.findById(id).map(OrderMapper::toOrdersDto).orElseThrow();
        List<ItemDto> itemDtoList = cartRepository.findByOrderId(ordersDto.getId()).stream().map(cart -> {
            ItemDto itemDto = itemRepository.findById(cart.getItemId()).map(ItemMapper::toItemDto).orElseThrow();
            itemDto.setCount(cart.getCount());
            return itemDto;
        }).toList();
        ordersDto.setItems(itemDtoList);
        Double totalSum = itemDtoList.stream().mapToDouble(itemDto -> itemDto.getCount() * itemDto.getPrice()).sum();
        ordersDto.setTotalSum(totalSum);
        return ordersDto;
    }

    public List<OrdersDto> getOrders() {
        return ordersRepository.findAll().stream().map(orders -> getOrder(orders.getId())).toList();
    }
}
