package com.intershop.configuration;

import com.intershop.client.ApiClient;
import com.intershop.client.api.PaymentApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class ApiClientConfig {

    @Value("${services.api.base-url:http://localhost:8081}")
    private String servicesBaseUrl;

    @Autowired
    private ReactiveOAuth2AuthorizedClientManager auth2AuthorizedClientManager;

/*
    @Bean
    public PaymentApi paymentApi() {
        PaymentApi api = new PaymentApi();
        api.getApiClient().setBasePath(servicesBaseUrl);
        return api;
    }
*/

    @Bean
    public PaymentApi paymentApi() {
        WebClient webClient = WebClient.builder()
                .filter(authFilter())
                .baseUrl(servicesBaseUrl)
                .build();

        ApiClient apiClient = new ApiClient(webClient);
        apiClient.setBasePath(servicesBaseUrl);

        return new PaymentApi(apiClient);
    }


    private ExchangeFilterFunction authFilter() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            return Mono.fromCallable(() -> OAuth2AuthorizeRequest
                            .withClientRegistrationId("keycloak")
                            .principal("system")
                            .build())
                    .flatMap(authRequest -> auth2AuthorizedClientManager.authorize(authRequest))
                    .map(client -> client.getAccessToken().getTokenValue())
                    .map(token -> ClientRequest.from(clientRequest)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .build()
                    );
        });
    }

}
