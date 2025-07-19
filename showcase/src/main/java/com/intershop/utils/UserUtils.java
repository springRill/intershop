package com.intershop.utils;

import com.intershop.configuration.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.Objects;

public class UserUtils {

    public static Mono<Long> getCurrentUserId(ServerRequest request) {
        return request.principal()
                .cast(Authentication.class)
                .map(auth -> ((CustomUserDetails) auth.getPrincipal()).getId())
                .defaultIfEmpty(Objects.requireNonNull(Mono.just(-1L).block()));
    }

}
