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
        OrdersDto ordersDtoFilled = ordersRepository.findById(id).map(orders -> {
            OrdersDto ordersDto = OrderMapper.toOrdersDto(orders);

            List<ItemDto> itemDtoList = orders.getCartList().stream().map(cart -> {
                ItemDto itemDto = ItemMapper.toItemDto(cart.getItem());
                itemDto.setCount(cart.getCount());
                return itemDto;
            }).toList();
            ordersDto.setItems(itemDtoList);

            Double totalSum = itemDtoList.stream().mapToDouble(itemDto -> itemDto.getCount() * itemDto.getPrice()).sum();
            ordersDto.setTotalSum(totalSum);

            return ordersDto;
        }).orElseThrow();

        return ordersDtoFilled;
    }

    public List<OrdersDto> getOrders() {
        return ordersRepository.findAll().stream().map(orders -> {
            OrdersDto ordersDto = OrderMapper.toOrdersDto(orders);

            List<ItemDto> itemDtoList = orders.getCartList().stream().map(cart -> {
                ItemDto itemDto = ItemMapper.toItemDto(cart.getItem());
                itemDto.setCount(cart.getCount());
                return itemDto;
            }).toList();
            ordersDto.setItems(itemDtoList);

            Double totalSum = itemDtoList.stream().mapToDouble(itemDto -> itemDto.getCount() * itemDto.getPrice()).sum();
            ordersDto.setTotalSum(totalSum);

            return ordersDto;
        }).toList();
    }
}
