package com.intershop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("cart")
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    @Id
    private Long id;

    private Long itemId;

    private Integer count = 0;

    private Long orderId;

}
