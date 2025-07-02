package com.intershop.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AccountService {

    private Double balance = 3000D;

    public Mono<Double> getBalance(){
        return Mono.just(balance);
    }

    public Mono<Void> pay(Double amount) {
        if (amount <= balance) {
            balance -= amount;
            return Mono.empty(); // успех
        } else {
            return Mono.error(new RuntimeException("Недостаточно средств на счёте"));
        }
    }

}
