package com.intershop.dto;

import lombok.Data;

import java.util.List;

@Data
public class ItemPageDto {

    private PagingDto pagingDto;
    private List<ItemDto> itemDtoList;

}
