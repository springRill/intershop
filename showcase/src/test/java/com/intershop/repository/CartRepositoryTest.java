package com.intershop.repository;

import com.intershop.initdb.InitTestDb;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.data.domain.Sort;
import reactor.test.StepVerifier;

@DataR2dbcTest
class CartRepositoryTest extends InitTestDb {

    @Autowired
    private CartRepository cartRepository;

    @Test
    void findByOrderIdIsNull() {
        StepVerifier.create(cartRepository.findByUserIdAndOrderIdIsNull(userId, Sort.by(Sort.Direction.ASC, "id")))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void findByItemIdAndOrderIdIsNull() {
        StepVerifier.create(cartRepository.findByItemIdAndUserIdAndOrderIdIsNull(1L, userId))
                .expectNextCount(0)
                .verifyComplete();

        StepVerifier.create(cartRepository.findByItemIdAndUserIdAndOrderIdIsNull(2L, userId))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void findByOrderId() {
        StepVerifier.create(cartRepository.findByOrderIdAndUserId(1L, userId))
                .expectNextCount(1)
                .verifyComplete();
    }
}