package com.intershop.service;

import com.intershop.domain.Orders;
import com.intershop.repository.CartRepository;
import com.intershop.repository.OrdersRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Service
public class BuyService {

    private final CartRepository cartRepository;

    private final OrdersRepository ordersRepository;

    private final TransactionalOperator transactionalOperator;

    public BuyService(CartRepository cartRepository, OrdersRepository ordersRepository, TransactionalOperator transactionalOperator) {
        this.cartRepository = cartRepository;
        this.ordersRepository = ordersRepository;
        this.transactionalOperator = transactionalOperator;
    }

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
