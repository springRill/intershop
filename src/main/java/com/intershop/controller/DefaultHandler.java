package com.intershop.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class DefaultHandler {

    public Mono<ServerResponse> homePage(ServerRequest request) {
        return ServerResponse.seeOther(URI.create("/main/items")).build();
    }
}
