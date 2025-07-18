package com.intershop.controller;

import com.intershop.configuration.RouterConfiguration;
import com.intershop.service.BuyService;
import com.intershop.service.ItemService;
import com.intershop.service.PaymentApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = BuyHandler.class)
@Import(RouterConfiguration.class)
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

/*
    @Test
    void buy() {
        Long orderId = 1L;

        when(itemService.getCartItems()).thenReturn(Flux.empty()); // или с нужным набором товаров

        when(paymentApiService.pay(anyDouble())).thenReturn(Mono.empty());

        when(buyService.buyCart()).thenReturn(Mono.just(orderId));

        webTestClient.post()
                .uri("/buy")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("Location", String.format("/orders/%d?newOrder=true", orderId));

    }
*/
}