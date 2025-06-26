package com.intershop.mapper;

import com.intershop.domain.Item;
import com.intershop.dto.ItemDto;

public class ItemMapper {

    public static ItemDto toItemDto(Item item){
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setTitle(item.getTitle());
        itemDto.setDescription(item.getDescription());
        itemDto.setImgPath(item.getImgPath());
        itemDto.setPrice(item.getPrice());
        return itemDto;
    }
}
