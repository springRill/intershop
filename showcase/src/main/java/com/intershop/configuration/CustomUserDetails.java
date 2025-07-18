package com.intershop.configuration;

import com.intershop.domain.AppUser;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class CustomUserDetails extends User {
    private final Long id;

    public CustomUserDetails(AppUser user) {
        super(user.getUsername(), user.getPassword(), List.of());
        this.id = user.getId();
    }

    public Long getId() {
        return id;
    }
}

