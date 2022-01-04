package com.api.conf;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.reactive.config.PathMatchConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Optional;

@Configuration
public class WebConfig implements WebFluxConfigurer {


    @Override
    public void configurePathMatching(PathMatchConfigurer configurer) {
        configurer
                .setUseCaseSensitiveMatch(true)
                .setUseTrailingSlashMatch(false)
                .addPathPrefix("/",
                        HandlerTypePredicate.forAnnotation(RestController.class));
    }

    @Bean
    CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration());
        return new CorsWebFilter(source) {
            public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
                var requestHeaders = exchange.getRequest().getHeaders();
                var responseHeaders = exchange.getResponse().getHeaders();
                String origin = Optional.ofNullable(requestHeaders.getOrigin()).orElse("*");
                var accReqHeaders = requestHeaders.getAccessControlRequestHeaders();
                var accReqMethod = requestHeaders.getAccessControlRequestMethod();
                responseHeaders.setAccessControlAllowCredentials(true);
                Optional.ofNullable(accReqMethod).map(Collections::singletonList).ifPresent(responseHeaders::setAccessControlAllowMethods);
                responseHeaders.setAccessControlAllowOrigin(origin);
                responseHeaders.setAccessControlAllowHeaders(accReqHeaders);
                return super.filter(exchange, chain);
            }
        };
    }

}