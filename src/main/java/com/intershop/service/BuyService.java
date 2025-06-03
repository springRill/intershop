package com.intershop.service;

import com.intershop.domain.Orders;
import com.intershop.repository.CartRepository;
import com.intershop.repository.OrdersRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class BuyService {

    CartRepository cartRepository;

    OrdersRepository ordersRepository;

    public BuyService(CartRepository cartRepository, OrdersRepository ordersRepository) {
        this.cartRepository = cartRepository;
        this.ordersRepository = ordersRepository;
    }

    @Transactional
    public Long buyCart(){
        Long orderId = ordersRepository.save(new Orders()).getId();
        cartRepository.findByOrderIdIsNull().forEach(cart -> {
            cart.setOrderId(orderId);
            cartRepository.save(cart);
        });
        return orderId;
    }
}
