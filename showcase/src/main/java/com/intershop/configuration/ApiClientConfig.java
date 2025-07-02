package com.intershop.configuration;

import com.intershop.client.api.PaymentApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class ApiClientConfig {

    @Value("${services.api.base-url:http://localhost:8081}")
    private String servicesBaseUrl;

    @Bean
    public PaymentApi paymentApi() {
        PaymentApi api = new PaymentApi();
        api.getApiClient().setBasePath(servicesBaseUrl);
        return api;
    }
}
