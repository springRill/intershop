package com.intershop.service;

import com.intershop.domain.Cart;
import com.intershop.dto.ItemActionEnum;
import com.intershop.initdb.InitTestDb;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class CartServiceTest extends InitTestDb {

    @Autowired
    private CartService cartService;

    @Test
    void changeCartItem() {
        assertEquals(2, entityManager.find(Cart.class, cartId).getCount());

        cartService.changeCartItem(itemInCartId, ItemActionEnum.PLUS);
        assertEquals(3, entityManager.find(Cart.class, cartId).getCount());

        cartService.changeCartItem(itemInCartId, ItemActionEnum.MINUS);
        assertEquals(2, entityManager.find(Cart.class, cartId).getCount());

        cartService.changeCartItem(itemInCartId, ItemActionEnum.DELETE);
        assertNull(entityManager.find(Cart.class, cartId));
    }
}