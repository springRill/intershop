package com.intershop.service;

import com.intershop.initdb.InitTestDb;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class ItemServiceTest extends InitTestDb {

    @Autowired
    private ItemService itemService;

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