package com.intershop.repository;

import com.intershop.initdb.InitTestDb;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import reactor.test.StepVerifier;

@DataR2dbcTest
class AppUserRepositoryTest extends InitTestDb {

    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    void findByUsername() {
        StepVerifier.create(appUserRepository.findByUsername("test_username"))
                .expectNextCount(1)
                .verifyComplete();
    }
}