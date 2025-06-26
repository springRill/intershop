package com.intershop.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrdersDto {

    private Long id;
    private List<ItemDto> items;
    private Double totalSum;

}
