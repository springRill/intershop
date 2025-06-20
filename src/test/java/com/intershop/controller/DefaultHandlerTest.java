package com.intershop.controller;

import com.intershop.configuration.RouterConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(controllers = DefaultHandler.class)
@Import(RouterConfiguration.class)
class DefaultHandlerTest {

    @MockitoBean
    private BuyHandler buyHandler;

    @MockitoBean
    private CartHandler cartHandler;

    @MockitoBean
    private ImageHandler imageHandler;

    @MockitoBean
    private ItemHandler itemHandler;

    @MockitoBean
    private MainHandler mainHandler;

    @MockitoBean
    private OrdersHandler ordersHandler;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void homePage() throws Exception {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("Location", "/main/items");
    }
}