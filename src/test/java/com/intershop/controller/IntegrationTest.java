package com.intershop.controller;

import com.intershop.dto.ItemActionEnum;
import com.intershop.repository.CartRepository;
import com.intershop.repository.OrdersRepository;
import com.intershop.service.InitServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class IntegrationTest extends InitServiceTest {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void buyControllerTest() throws Exception {
        assertEquals(1, ordersRepository.findAll().size());

        mockMvc.perform(post("/buy"))
                .andExpect(status().is3xxRedirection());

        assertEquals(2, ordersRepository.findAll().size());
    }

    @Test
    void cartControllerTest() throws Exception {
        assertEquals(2, cartRepository.findById(itemInCartId).orElseThrow().getCount());

        mockMvc.perform(post("/cart/items/%d".formatted(itemInCartId))
                        .param("action", ItemActionEnum.PLUS.name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart/items"));

        assertEquals(3, cartRepository.findById(itemInCartId).orElseThrow().getCount());
    }

    @Test
    void itemControllerTest() throws Exception {
        assertEquals(2, cartRepository.findById(itemInCartId).orElseThrow().getCount());

        mockMvc.perform(post("/items/%d".formatted(itemInCartId))
                        .param("action", ItemActionEnum.MINUS.name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items/%d".formatted(itemInCartId)));

        assertEquals(1, cartRepository.findById(itemInCartId).orElseThrow().getCount());
    }

    @Test
    void mainControllerTest() throws Exception {
        assertEquals(2, cartRepository.findById(itemInCartId).orElseThrow().getCount());

        mockMvc.perform(post("/main/items/%d".formatted(itemInCartId))
                        .param("action", ItemActionEnum.DELETE.name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/main/items"));

        assertThat(cartRepository.findById(itemInCartId)).isEmpty();
    }

}
