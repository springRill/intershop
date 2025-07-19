package com.intershop.service;

import com.intershop.initdb.InitTestDb;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemServiceTest extends InitTestDb {

    @Autowired
    private ItemService itemService;

    @MockitoBean
    private PaymentApiService paymentApiService;

    @Test
    void findByTitle() {
        Pageable pageable = PageRequest.of(0, 10);

        StepVerifier.create(itemService.findByTitle(userId, "item", pageable))
                .assertNext(itemPageDto -> assertEquals(2, itemPageDto.getItemDtoList().size()))
                .verifyComplete();

        StepVerifier.create(itemService.findByTitle(userId, "item_1 title", pageable))
                .assertNext(itemPageDto -> assertEquals(1, itemPageDto.getItemDtoList().size()))
                .verifyComplete();

        StepVerifier.create(itemService.findByTitle(userId, "item_2 title", pageable))
                .assertNext(itemPageDto -> assertEquals(1, itemPageDto.getItemDtoList().size()))
                .verifyComplete();
    }

    @Test
    void getCartItems() {
        StepVerifier.create(itemService.getCartItemsByUserId(userId))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void findByItemId() {
        StepVerifier.create(itemService.findByItemIdAndUserId(itemInCartId, userId))
                .assertNext(itemDto -> assertEquals(2, itemDto.getCount()))
                .verifyComplete();
    }
}