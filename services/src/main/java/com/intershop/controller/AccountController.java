package com.intershop.controller;

import com.intershop.server.domain.BalanceGetRequest;
import com.intershop.server.domain.PayPutRequest;
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
    public Mono<ResponseEntity<Double>> balanceGet(Mono<BalanceGetRequest> balanceGetRequest, ServerWebExchange exchange) {
        return balanceGetRequest.flatMap(request -> accountService.getBalance(request.getUserId())).map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Void>> payPut(Mono<PayPutRequest> payPutRequest, ServerWebExchange exchange) {
        return payPutRequest.flatMap(request -> accountService.pay(request.getAmount(), request.getUserId()))
                .thenReturn(ResponseEntity.ok().<Void>build())
                .onErrorResume(e -> Mono.just(ResponseEntity.status(402).build()));
    }
}
