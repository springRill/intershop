package com.intershop.controller;

import com.intershop.configuration.CustomUserDetails;
import com.intershop.domain.AppUser;
import com.intershop.domain.Cart;
import com.intershop.dto.ItemActionEnum;
import com.intershop.initdb.InitTestDb;
import com.intershop.repository.AppUserRepository;
import com.intershop.repository.CartRepository;
import com.intershop.repository.OrdersRepository;
import com.intershop.service.PaymentApiService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class IntegrationTest extends InitTestDb {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private PaymentApiService paymentApiService;

    @MockitoBean
    private AppUserRepository repository;

    private static CustomUserDetails userDetails;

    @BeforeAll
    static void setupUser() {
        userDetails = new CustomUserDetails(
                new AppUser(1L, "test3", "password")
        );
    }

    @Test
    void buyIntegrationTest() {
        when(paymentApiService.pay(anyDouble(), anyLong())).thenReturn(Mono.empty());

        int before = ordersRepository.findAll().collectList().block().size();

        webTestClient
                .mutateWith(SecurityMockServerConfigurers.mockUser(userDetails))
                .post()
                .uri("/buy")
                .exchange()
                .expectStatus().is3xxRedirection();

        int after = ordersRepository.findAll().collectList().block().size();
        assertEquals(before + 1, after);
    }

    @Test
    void cartIntegrationTest() {
        assertEquals(2, cartRepository.findById(itemInCartId).block().getCount());

        webTestClient
                .mutateWith(SecurityMockServerConfigurers.mockUser(userDetails))
                .post()
                .uri("/cart/items/{id}", itemInCartId)
                .bodyValue("action=PLUS")
                .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("Location", "/cart/items");

        assertEquals(3, cartRepository.findById(itemInCartId).block().getCount());
    }

    @Test
    void itemIntegrationTest() {
        int before = cartRepository.findById(itemInCartId).map(Cart::getCount).block();
        assertEquals(2, before);

        webTestClient
                .mutateWith(SecurityMockServerConfigurers.mockUser(userDetails))
                .post()
                .uri("/items/{id}", itemInCartId)
                .body(BodyInserters.fromFormData("action", ItemActionEnum.MINUS.name()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("Location", "/items/%d".formatted(itemInCartId));

        int after = cartRepository.findById(itemInCartId).map(Cart::getCount).block();
        assertEquals(1, after);
    }

    @Test
    void mainIntegrationTest() {
        Integer before = cartRepository.findById(itemInCartId).map(Cart::getCount).block();
        assertEquals(2, before);

        webTestClient
                .mutateWith(SecurityMockServerConfigurers.mockUser(userDetails))
                .post()
                .uri("/main/items/{id}", itemInCartId)
                .body(BodyInserters.fromFormData("action", ItemActionEnum.DELETE.name()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("Location", "/main/items");

        Cart cart = cartRepository.findById(itemInCartId).block();
        assertNull(cart);
    }

}
