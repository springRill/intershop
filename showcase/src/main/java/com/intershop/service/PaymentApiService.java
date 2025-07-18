package com.intershop.service;

import com.intershop.client.api.PaymentApi;
import com.intershop.client.domain.BalanceGetRequest;
import com.intershop.client.domain.PayPutRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PaymentApiService {

    private final PaymentApi paymentApi;

    public PaymentApiService(PaymentApi paymentApi) {
        this.paymentApi = paymentApi;
    }

    public Mono<Double> getBalance(Long userId){
        BalanceGetRequest balanceGetRequest = new BalanceGetRequest();
        balanceGetRequest.setUserId(userId);
        return paymentApi.balanceGet(balanceGetRequest);
    }

    public Mono<Void> pay(Double amount, Long userId){
        PayPutRequest payPutRequest = new PayPutRequest();
        payPutRequest.setAmount(amount);
        payPutRequest.setUserId(userId);
        return paymentApi.payPut(payPutRequest);
    }


}
