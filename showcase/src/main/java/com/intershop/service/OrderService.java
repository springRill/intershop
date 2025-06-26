package com.intershop.service;

import com.intershop.dto.ItemDto;
import com.intershop.dto.OrdersDto;
import com.intershop.mapper.ItemMapper;
import com.intershop.mapper.OrderMapper;
import com.intershop.repository.CartRepository;
import com.intershop.repository.ItemRepository;
import com.intershop.repository.OrdersRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderService {

    private final OrdersRepository ordersRepository;

    private final CartRepository cartRepository;

    private final ItemRepository itemRepository;

    public OrderService(OrdersRepository ordersRepository, CartRepository cartRepository, ItemRepository itemRepository) {
        this.ordersRepository = ordersRepository;
        this.cartRepository = cartRepository;
        this.itemRepository = itemRepository;
    }

    public Mono<OrdersDto> getOrder(Long id) {
        return ordersRepository.findById(id)
                .switchIfEmpty(Mono.error(new Exception("Order not found")))
                .flatMap(order ->
                        cartRepository.findByOrderId(order.getId()) // Flux<Cart>
                                .flatMap(cart ->
                                        itemRepository.findById(cart.getItemId())
                                                .map(item -> {
                                                    ItemDto itemDto = ItemMapper.toItemDto(item);
                                                    itemDto.setCount(cart.getCount());
                                                    return itemDto;
                                                })
                                )
                                .collectList()
                                .map(itemDtoList -> {
                                    OrdersDto ordersDto = OrderMapper.toOrdersDto(order);
                                    ordersDto.setItems(itemDtoList);

                                    double totalSum = itemDtoList.stream()
                                            .mapToDouble(dto -> dto.getPrice() * dto.getCount())
                                            .sum();
                                    ordersDto.setTotalSum(totalSum);

                                    return ordersDto;
                                })
                );
    }

    public Flux<OrdersDto> getOrders() {
        return ordersRepository.findAll()
                .flatMap(order ->
                        cartRepository.findByOrderId(order.getId())
                                .flatMap(cart ->
                                        itemRepository.findById(cart.getItemId())
                                                .map(item -> {
                                                    ItemDto itemDto = ItemMapper.toItemDto(item);
                                                    itemDto.setCount(cart.getCount());
                                                    return itemDto;
                                                })
                                )
                                .collectList()
                                .map(itemDtoList -> {
                                    OrdersDto ordersDto = OrderMapper.toOrdersDto(order);
                                    ordersDto.setItems(itemDtoList);

                                    double totalSum = itemDtoList.stream()
                                            .mapToDouble(dto -> dto.getCount() * dto.getPrice())
                                            .sum();
                                    ordersDto.setTotalSum(totalSum);

                                    return ordersDto;
                                })
                );
    }

}
