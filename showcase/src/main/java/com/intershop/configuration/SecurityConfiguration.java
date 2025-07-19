package com.intershop.configuration;

import com.intershop.repository.AppUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.net.URI;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    @Bean
    ReactiveOAuth2AuthorizedClientManager auth2AuthorizedClientManager(
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ReactiveOAuth2AuthorizedClientService authorizedClientService
    ) {
        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager manager =
                new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientService);

        manager.setAuthorizedClientProvider(ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .refreshToken()
                .build()
        );

        return manager;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/cart/**").authenticated()
                        .pathMatchers(HttpMethod.POST, "/items/**").authenticated()
                        .pathMatchers(HttpMethod.POST, "/main/items/**").authenticated()
                        .pathMatchers("/orders/**").authenticated()
                        .anyExchange().permitAll())
//                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .logout(logout -> logout
                        .logoutSuccessHandler((webFilterExchange, authentication) ->
                                {
                                    ServerWebExchange exchange = webFilterExchange.getExchange();

                                    return exchange.getSession()
                                            .flatMap(WebSession::invalidate)
                                            .then(Mono.fromRunnable(() -> {
                                                ResponseCookie deleteCookie = ResponseCookie.from("JSESSIONID", "")
                                                        .path("/")
                                                        .maxAge(0)
                                                        .build();
                                                exchange.getResponse().addCookie(deleteCookie);

                                                exchange.getResponse().setStatusCode(HttpStatus.SEE_OTHER);
                                                exchange.getResponse().getHeaders().setLocation(URI.create("/"));
                                            }));

                                }
                        )
                )
                .build();
    }

    @Bean
    public ReactiveUserDetailsService userDetailsService(AppUserRepository repository) {
        return username -> repository.findByUsername(username).map(CustomUserDetails::new);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
