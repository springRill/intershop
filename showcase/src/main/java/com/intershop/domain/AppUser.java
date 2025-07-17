package com.intershop.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@Table("users")
public class AppUser {

    @Id
    private Long id;

    private String username;

    private String password;

}
