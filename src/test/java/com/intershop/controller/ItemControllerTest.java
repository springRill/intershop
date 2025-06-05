package com.intershop.controller;

import com.intershop.dto.ItemActionEnum;
import com.intershop.dto.ItemDto;
import com.intershop.service.CartService;
import com.intershop.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @MockitoBean
    private ItemService itemService;

    @MockitoBean
    private CartService cartService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getItems() throws Exception {
        ItemDto itemDto = new ItemDto();

        when(itemService.findByItemId(1L)).thenReturn(itemDto);

        mockMvc.perform(get("/items/1"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("item"))
                .andExpect(model().attributeExists("item"))
                .andExpect(status().isOk());
    }

    @Test
    void changeCartItem() throws Exception {
        mockMvc.perform(post("/items/1")
                        .param("action", ItemActionEnum.PLUS.name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items/1"));
    }
}