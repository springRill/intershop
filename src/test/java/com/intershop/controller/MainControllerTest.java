package com.intershop.controller;

import com.intershop.dto.ItemActionEnum;
import com.intershop.dto.ItemDto;
import com.intershop.dto.ItemPageDto;
import com.intershop.dto.PagingDto;
import com.intershop.service.CartService;
import com.intershop.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MainController.class)
class MainControllerTest {

    @MockitoBean
    private ItemService itemService;

    @MockitoBean
    private CartService cartService;

    @Autowired
    private MockMvc mockMvc;

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
        when(itemService.findByTitle("", pageable)).thenReturn(itemPageDto);

        mockMvc.perform(get("/main/items")
                        .param("search", ""))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("main"))
                .andExpect(model().attributeExists("items"))
                .andExpect(model().attributeExists("search"))
                .andExpect(model().attributeExists("sort"))
                .andExpect(model().attributeExists("paging"))
                .andExpect(status().isOk());
    }

    @Test
    void changeCartItem() throws Exception {
        mockMvc.perform(post("/main/items/1")
                        .param("action", ItemActionEnum.PLUS.name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/main/items"));
    }
}