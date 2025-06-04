package com.intershop.service;

import com.intershop.domain.Cart;
import com.intershop.domain.Item;
import com.intershop.domain.Orders;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/*
@SpringBootTest
@ActiveProfiles("test")
@Transactional
*/
class ItemServiceTest extends InitServiceTest {

    @Autowired
    ItemService itemService;

    @Test
    void findByTitle() {
        assertEquals(2, itemService.findByTitle("item", null).getItemDtoList().size());
        assertEquals(1, itemService.findByTitle("item_1 title", null).getItemDtoList().size());
        assertEquals(1, itemService.findByTitle("item_2 title", null).getItemDtoList().size());
    }

    @Test
    void getCartItems() {
        assertEquals(1, itemService.getCartItems().size());
    }

    @Test
    void findByItemId() {
        assertEquals(2, itemService.findByItemId(itemInCartId).getCount());
    }
}