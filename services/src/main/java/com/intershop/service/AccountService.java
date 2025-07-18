package com.intershop.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class AccountService {

    private Map<Long, Double> balanceMap = new HashMap<>();

    private Double getUserBalance(Long userId){
        if(!balanceMap.containsKey(userId)){
            balanceMap.put(userId, 3000D);
        }
        return balanceMap.get(userId);
    }

    public Mono<Double> getBalance(Long userId){
        return Mono.just(getUserBalance(userId));
    }

    public Mono<Void> pay(Double amount, Long userId) {
        if (amount <= getUserBalance(userId)) {
            balanceMap.put(userId, balanceMap.get(userId) - amount);
            return Mono.empty(); // успех
        } else {
            return Mono.error(new RuntimeException("Недостаточно средств на счёте"));
        }
    }

}
