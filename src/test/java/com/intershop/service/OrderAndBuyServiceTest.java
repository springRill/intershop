package com.intershop.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class OrderAndBuyServiceTest extends InitServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    BuyService buyService;

    @Test
    void buyAndOrder() {
        assertEquals(1, orderService.getOrders().size());
        assertNotNull(orderService.getOrder(orderId));

        buyService.buyCart();
        assertEquals(2, orderService.getOrders().size());
    }
}