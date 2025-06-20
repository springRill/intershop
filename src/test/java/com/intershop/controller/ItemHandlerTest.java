package com.intershop.controller;

import com.intershop.configuration.RouterConfiguration;
import com.intershop.dto.ItemActionEnum;
import com.intershop.dto.ItemDto;
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
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = ItemHandler.class)
@Import(RouterConfiguration.class)
class ItemHandlerTest {

    @MockitoBean
    private BuyHandler buyHandler;

    @MockitoBean
    private CartHandler cartHandler;

    @MockitoBean
    private ImageHandler imageHandler;

    @MockitoBean
    private DefaultHandler defaultHandler;

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
    void getItems() {
        Long itemId = 1L;

        ItemDto itemDto = new ItemDto();

        when(itemService.findByItemId(itemId)).thenReturn(Mono.just(itemDto));
        webTestClient.get()
                .uri("/items/{id}", itemId)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.TEXT_HTML)
                .expectBody(String.class)
                .consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                    assertTrue(body.contains("item"));
                });
    }


    @Test
    void changeCartItem() {
        Long itemId = 1L;
        ItemActionEnum action = ItemActionEnum.PLUS;

        when(cartService.changeCartItem(itemId, action)).thenReturn(Mono.empty());
        webTestClient.post()
                .uri("/items/{id}", itemId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("action", action.name()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("Location", String.format("/items/%d", itemId));
    }

}