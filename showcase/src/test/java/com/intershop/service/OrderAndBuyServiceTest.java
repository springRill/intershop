package com.intershop.service;

import com.intershop.initdb.InitTestDb;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.test.StepVerifier;

@SpringBootTest
class OrderAndBuyServiceTest extends InitTestDb {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BuyService buyService;

    @MockitoBean
    private PaymentApiService paymentApiService;

    @Test
    void buyAndOrder() {
        StepVerifier.create(orderService.getOrders())
                .expectNextCount(1)
                .verifyComplete();

        StepVerifier.create(orderService.getOrder(orderId))
                .assertNext(Assertions::assertNotNull)
                .verifyComplete();

        StepVerifier.create(buyService.buyCart())
                .expectNextCount(1)
                .verifyComplete();

        StepVerifier.create(orderService.getOrders())
                .expectNextCount(2)
                .verifyComplete();
    }
}