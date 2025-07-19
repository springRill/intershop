package com.intershop.utils;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public class RenderUtils {

    public static Mono<ServerResponse> render(String template, Map<String, Object> model) {
        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> ctx.getAuthentication())
                .flatMap(auth -> {
                    Map<String, Object> extendedModel = new HashMap<>(model);
                    extendedModel.put("auth", auth);
                    extendedModel.put("isAuthenticated", auth != null && auth.isAuthenticated());
                    return ServerResponse.ok().render(template, extendedModel);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    Map<String, Object> extendedModel = new HashMap<>(model);
                    extendedModel.put("auth", null);
                    extendedModel.put("isAuthenticated", false);
                    return ServerResponse.ok().render(template, extendedModel);
                }));
    }

}
