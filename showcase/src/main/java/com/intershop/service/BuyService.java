package com.intershop.service;

import com.intershop.domain.Orders;
import com.intershop.repository.CartRepository;
import com.intershop.repository.OrdersRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class BuyService {

    private final CartRepository cartRepository;

    private final OrdersRepository ordersRepository;

    public BuyService(CartRepository cartRepository, OrdersRepository ordersRepository) {
        this.cartRepository = cartRepository;
        this.ordersRepository = ordersRepository;
    }

    @Caching(evict = {
            @CacheEvict(value = "itemPages", allEntries = true),
            @CacheEvict(value = "carts", allEntries = true)
    })
    public Mono<Long> buyCart() {
        return ordersRepository.save(new Orders())
                .flatMap(order ->
                        cartRepository.findByOrderIdIsNull(Sort.by(Sort.Direction.ASC, "id"))
                                .flatMap(cart -> {
                                    cart.setOrderId(order.getId());
                                    return cartRepository.save(cart);
                                })
                                .then(Mono.just(order.getId()))
                );
    }
}
