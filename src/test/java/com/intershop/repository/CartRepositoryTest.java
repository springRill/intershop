package com.intershop.repository;

import com.intershop.domain.Cart;
import com.intershop.initdb.InitRepositoryTestDb;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class CartRepositoryTest extends InitRepositoryTestDb {

    @Autowired
    private CartRepository cartRepository;

    @Test
    void findByOrderIdIsNull() {
        assertEquals(1, cartRepository.findByOrderIdIsNull().size());
    }

    @Test
    void findByItemIdAndOrderIdIsNull() {
        Cart cartWithNullOrder = cartRepository.findAll().stream().filter(cart -> cart.getOrderId() == null).findFirst().orElseThrow();
        assertEquals(1, cartRepository.findByItemIdAndOrderIdIsNull(cartWithNullOrder.getItemId()).size());

        Cart cartWithNotNullOrder = cartRepository.findAll().stream().filter(cart -> cart.getOrderId() != null).findFirst().orElseThrow();
        assertEquals(0, cartRepository.findByItemIdAndOrderIdIsNull(cartWithNotNullOrder.getItemId()).size());
    }

    @Test
    void findByOrderId() {
        Cart cartWithOrderId = cartRepository.findAll().stream().filter(cart -> cart.getOrderId()!=null).findFirst().orElseThrow();
        assertEquals(1, cartRepository.findByOrderId(cartWithOrderId.getOrderId()).size());
    }
}