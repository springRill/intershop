package com.intershop.service;

import com.intershop.initdb.InitTestDb;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class OrderAndBuyServiceTest extends InitTestDb {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BuyService buyService;

    @Test
    void buyAndOrder() {
        assertEquals(1, orderService.getOrders().size());
        assertNotNull(orderService.getOrder(orderId));

        buyService.buyCart();
        assertEquals(2, orderService.getOrders().size());
    }
}