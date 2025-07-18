package com.intershop.controller;

import com.intershop.configuration.RouterConfiguration;
import com.intershop.dto.ItemActionEnum;
import com.intershop.dto.ItemDto;
import com.intershop.dto.ItemPageDto;
import com.intershop.dto.PagingDto;
import com.intershop.service.CartService;
import com.intershop.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.when;


@WebFluxTest(controllers = MainHandler.class)
@Import(RouterConfiguration.class)
class MainHandlerTest {

    @MockitoBean
    private BuyHandler buyHandler;

    @MockitoBean
    private CartHandler cartHandler;

    @MockitoBean
    private ImageHandler imageHandler;

    @MockitoBean
    private DefaultHandler defaultHandler;

    @MockitoBean
    private ItemHandler itemHandler;

    @MockitoBean
    private OrdersHandler ordersHandler;

    @MockitoBean
    private ItemService itemService;

    @MockitoBean
    private CartService cartService;

    @Autowired
    private WebTestClient webTestClient;

/*
    @Test
    void getItems() throws Exception {
        ItemDto itemDto_1 = new ItemDto();
        ItemDto itemDto_2 = new ItemDto();

        PagingDto pagingDto = new PagingDto();
        pagingDto.setPageSize(10);
        pagingDto.setHasPrevious(false);
        pagingDto.setHasNext(false);
        pagingDto.setPageNumber(1);

        ItemPageDto itemPageDto = new ItemPageDto();
        itemPageDto.setItemDtoList(List.of(itemDto_1, itemDto_2));
        itemPageDto.setPagingDto(pagingDto);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        when(itemService.findByTitle("", pageable)).thenReturn(Mono.just(itemPageDto));
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/main/items")
                        .queryParam("search", "")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.TEXT_HTML)
                .expectBody();
    }
*/


/*
    @Test
    void changeCartItem() {
        Long itemId = 1L;
        ItemActionEnum action = ItemActionEnum.PLUS;

        when(cartService.changeCartItem(itemId, action)).thenReturn(Mono.empty());
        webTestClient.post()
                .uri("/main/items/{id}", itemId)  // без query параметров
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("action", action.name()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", "/main/items");
    }
*/

}