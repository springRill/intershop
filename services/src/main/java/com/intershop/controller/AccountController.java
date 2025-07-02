package com.intershop.controller;

import com.intershop.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import com.intershop.server.api.PaymentApi;

@RestController
public class AccountController implements PaymentApi {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public Mono<ResponseEntity<Double>> balanceGet(ServerWebExchange exchange) {
        return accountService.getBalance().map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Void>> payPut(Mono<Double> body, ServerWebExchange exchange) {
        return body
                .flatMap(accountService::pay)
                .thenReturn(ResponseEntity.ok().<Void>build())
                .onErrorResume(e -> Mono.just(ResponseEntity.status(402).build()));
    }
}
