package com.intershop.service;

import com.intershop.dto.ItemActionEnum;
import com.intershop.initdb.InitTestDb;
import com.intershop.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartServiceTest extends InitTestDb {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;

    @MockitoBean
    private PaymentApiService paymentApiService;

    @Test
    void changeCartItem() {

        StepVerifier.create(cartRepository.findById(cartId))
                .assertNext(cart -> assertEquals(2, cart.getCount()))
                .verifyComplete();

        StepVerifier.create(cartService.changeCartItem(itemInCartId, ItemActionEnum.PLUS))
                .verifyComplete();

        StepVerifier.create(cartRepository.findById(cartId))
                .assertNext(cart -> assertEquals(3, cart.getCount()))
                .verifyComplete();

        StepVerifier.create(cartService.changeCartItem(itemInCartId, ItemActionEnum.MINUS))
                .verifyComplete();

        StepVerifier.create(cartRepository.findById(cartId))
                .assertNext(cart -> assertEquals(2, cart.getCount()))
                .verifyComplete();

        StepVerifier.create(cartService.changeCartItem(itemInCartId, ItemActionEnum.DELETE))
                .verifyComplete();

        StepVerifier.create(cartRepository.findById(cartId))
                .expectNextCount(0)
                .verifyComplete();
    }
}