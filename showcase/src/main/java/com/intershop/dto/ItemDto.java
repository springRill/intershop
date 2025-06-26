package com.intershop.dto;

import lombok.Data;

@Data
public class ItemDto {

    private Long id;
    private String title;
    private String description;
    private String imgPath;
    private Integer count;
    private Double price;

}
