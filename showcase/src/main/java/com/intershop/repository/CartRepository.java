package com.intershop.repository;

import com.intershop.domain.Cart;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface CartRepository extends R2dbcRepository<Cart, Long> {

    Flux<Cart> findByUserIdAndOrderIdIsNull(Long userId, Sort sort);

    Flux<Cart> findByItemIdAndUserIdAndOrderIdIsNull(Long itemId, Long userId);

    Flux<Cart> findByOrderIdAndUserId(Long orderId, Long userId);

}
