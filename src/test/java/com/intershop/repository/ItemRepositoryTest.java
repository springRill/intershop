package com.intershop.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class ItemRepositoryTest extends InitRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void findByTitleContaining() {
        assertEquals(2, itemRepository.findAll().size());
        assertEquals(2, itemRepository.findByTitleContaining("title", null).getSize());
        assertEquals(1, itemRepository.findByTitleContaining("item_1 title", null).getSize());
        assertEquals(1, itemRepository.findByTitleContaining("item_2 title", null).getSize());
    }
}