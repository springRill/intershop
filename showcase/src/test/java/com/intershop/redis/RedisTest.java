package com.intershop.redis;

import com.intershop.configuration.EmbeddedRedisConfiguration;
import com.intershop.dto.ItemDto;
import com.intershop.dto.ItemPageDto;
import com.intershop.service.ItemService;
import com.intershop.service.PaymentApiService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("redisTest")
@Import(EmbeddedRedisConfiguration.class)
public class RedisTest {

    @SpyBean
    private ItemService itemService;

    @MockitoBean
    private PaymentApiService paymentApiService;

/*
    @Test
    public void testFindByTitle() {
        Pageable pageable = PageRequest.of(0, 10);

        ItemPageDto itemPageDto1 = itemService.findByTitle("item", pageable).block();
        assertNotNull(itemPageDto1);
        verify(itemService, times(1)).findByTitle("item", pageable);

        ItemPageDto itemPageDto2 = itemService.findByTitle("item", pageable).block();
        assertNotNull(itemPageDto2);
        verify(itemService, times(1)).findByTitle("item", pageable);

    }

    @Test
    public void testGetCartItems() {
        List<ItemDto> itemDtoList1 = itemService.getCartItems().collectList().block();
        assertNotNull(itemDtoList1);
        verify(itemService, times(1)).getCartItems();

        List<ItemDto> itemDtoList2 = itemService.getCartItems().collectList().block();
        assertNotNull(itemDtoList2);
        verify(itemService, times(1)).getCartItems();
    }

    @Test
    public void testFindByItemId() {
        ItemDto itemDto1 = itemService.findByItemId(1L).block();
        assertNotNull(itemDto1);
        verify(itemService, times(1)).findByItemId(1L);

        ItemDto itemDto2 = itemService.findByItemId(1L).block();
        assertNotNull(itemDto2);
        verify(itemService, times(1)).findByItemId(1L);
    }
*/
}
