package com.intershop.controller;

import com.intershop.configuration.RouterConfiguration;
import com.intershop.dto.ItemActionEnum;
import com.intershop.service.CartService;
import com.intershop.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = CartHandler.class)
@Import(RouterConfiguration.class)
class CartHandlerTest {

    @MockitoBean
    private BuyHandler buyHandler;

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
    private ItemService itemService;

    @MockitoBean
    private CartService cartService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void getCartItems() {
        when(itemService.getCartItems()).thenReturn(Flux.empty());

        webTestClient.get()
                .uri("/cart/items")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.TEXT_HTML)
                .expectBody(String.class)
                .consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                    assertTrue(body.contains("items"));
                });
    }

    @Test
    void changeCartItem() throws Exception {

        Long itemId = 1L;
        ItemActionEnum action = ItemActionEnum.PLUS;

        when(cartService.changeCartItem(itemId, action)).thenReturn(Mono.empty());
        webTestClient.post()
                .uri("/cart/items/{id}", itemId)  // без query параметров
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("action", action.name()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", "/cart/items");
    }
}