package com.intershop.controller;

import com.intershop.configuration.CustomUserDetails;
import com.intershop.configuration.RouterConfiguration;
import com.intershop.configuration.SecurityConfiguration;
import com.intershop.domain.AppUser;
import com.intershop.repository.AppUserRepository;
import com.intershop.service.BuyService;
import com.intershop.service.ItemService;
import com.intershop.service.PaymentApiService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = BuyHandler.class)
@Import({RouterConfiguration.class, SecurityConfiguration.class})
class BuyHandlerTest {

    @MockitoBean
    private CartHandler cartHandler;

    @MockitoBean
    private DefaultHandler defaultHandler;

    @MockitoBean
    private ImageHandler imageHandler;

    @MockitoBean
    private ItemHandler itemHandler;

    @MockitoBean
    private MainHandler mainHandler;

    @MockitoBean
    private OrdersHandler ordersHandler;

    @MockitoBean
    private BuyService buyService;

    @MockitoBean
    private ItemService itemService;

    @MockitoBean
    private PaymentApiService paymentApiService;


    @Autowired
    private WebTestClient webTestClient;

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
    void buy() {
        Long orderId = 1L;

        when(itemService.getCartItemsByUserId(1L)).thenReturn(Flux.empty()); // или с нужным набором товаров
        when(paymentApiService.pay(anyDouble(), anyLong())).thenReturn(Mono.empty());
        when(buyService.buyCart(1L)).thenReturn(Mono.just(orderId));

        webTestClient
                .mutateWith(SecurityMockServerConfigurers.mockUser(userDetails))
                .post()
                .uri("/buy")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("Location", String.format("/orders/%d?newOrder=true", orderId));

    }
}