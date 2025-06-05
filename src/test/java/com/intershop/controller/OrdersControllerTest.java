package com.intershop.controller;

import com.intershop.dto.OrdersDto;
import com.intershop.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrdersController.class)
class OrdersControllerTest {

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getOrders() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("orders"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(status().isOk());
    }

    @Test
    void getOrder() throws Exception {
        OrdersDto ordersDto = new OrdersDto();
        when(orderService.getOrder(1L)).thenReturn(ordersDto);

        mockMvc.perform(get("/orders/1"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("order"))
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attributeExists("newOrder"))
                .andExpect(status().isOk());
    }
}