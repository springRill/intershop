package com.intershop.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long itemId;
    Integer count = 0;
    Long orderId;

}
