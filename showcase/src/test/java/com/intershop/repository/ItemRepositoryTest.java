package com.intershop.repository;

import com.intershop.initdb.InitTestDb;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import reactor.test.StepVerifier;

@DataR2dbcTest
class ItemRepositoryTest extends InitTestDb {

    @Autowired
    private ItemRepository itemRepository;

/*
    @Test
    void findByTitleContaining() {
        StepVerifier.create(itemRepository.findAll())
                .expectNextCount(2)
                .verifyComplete();

        StepVerifier.create(itemRepository.findByTitleContaining("title", null))
                .expectNextCount(2)
                .verifyComplete();


        StepVerifier.create(itemRepository.findByTitleContaining("item_1 title", null))
                .expectNextCount(1)
                .verifyComplete();

        StepVerifier.create(itemRepository.findByTitleContaining("item_2 title", null))
                .expectNextCount(1)
                .verifyComplete();
    }
*/
}