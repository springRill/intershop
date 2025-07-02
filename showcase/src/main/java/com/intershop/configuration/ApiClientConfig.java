package com.intershop.configuration;

import com.intershop.client.api.PaymentApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiClientConfig {

    @Bean
    public PaymentApi paymentApi() {
        PaymentApi api = new PaymentApi();
        api.getApiClient().setBasePath("http://localhost:8081");
        return api;
    }
}
