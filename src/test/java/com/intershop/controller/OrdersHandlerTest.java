package com.intershop.controller;

import com.intershop.dto.OrdersDto;
import com.intershop.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = OrdersHandler.class)
@Import(OrdersRouterConfiguration.class)
class OrdersHandlerTest {

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void getOrders() {
        List<OrdersDto> mockOrders = List.of(new OrdersDto(), new OrdersDto());
        when(orderService.getOrders()).thenReturn(Flux.fromIterable(mockOrders));

        webTestClient.get()
                .uri("/orders")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                    assertTrue(body.contains("orders"));
                });
    }

    @Test
    void getOrder() {
        Long orderId = 1L;

        OrdersDto ordersDto = new OrdersDto();
        when(orderService.getOrder(orderId)).thenReturn(Mono.just(ordersDto));

        webTestClient.get()
                .uri(String.format("/orders/%d?newOrder=true", orderId))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                    assertTrue(body.contains("order"));
                });
    }

}