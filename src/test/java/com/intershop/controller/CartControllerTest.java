package com.intershop.controller;

import com.intershop.dto.ItemActionEnum;
import com.intershop.service.CartService;
import com.intershop.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @MockitoBean
    private ItemService itemService;

    @MockitoBean
    private CartService cartService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getCartItems() throws Exception {
        mockMvc.perform(get("/cart/items"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("cart"))
                .andExpect(model().attributeExists("items"))
                .andExpect(model().attributeExists("total"))
                .andExpect(model().attributeExists("empty"))
                .andExpect(status().isOk());
    }

    @Test
    void changeCartItem() throws Exception {
        mockMvc.perform(post("/cart/items/1")
                        .param("action", ItemActionEnum.PLUS.name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart/items"));
    }
}