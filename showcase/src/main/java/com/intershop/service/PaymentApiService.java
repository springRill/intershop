package com.intershop.service;

import com.intershop.client.api.PaymentApi;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PaymentApiService {

    private final PaymentApi paymentApi;

    public PaymentApiService(PaymentApi paymentApi) {
        this.paymentApi = paymentApi;
    }

    public Mono<Double> getBalance(){
        return paymentApi.balanceGet();
    }

    public Mono<Void> pay(Double amount){
        return paymentApi.payPut(amount);
    }
}
