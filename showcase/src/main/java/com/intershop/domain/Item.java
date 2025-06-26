package com.intershop.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@Table("item")
public class Item {

    @Id
    private Long id;

    private String title;

    private String description;

    private String imgPath;

    private Double price;

    public Item(Long id) {
        this.id = id;
    }

}
