package com.api.apigateway.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Configuration
public class CorsConfig {
    @Bean
    public WebFilter corsFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (CorsUtils.isCorsRequest(request)) {
                HttpHeaders headers = request.getHeaders();
                ServerHttpResponse response = exchange.getResponse();
                HttpMethod requestMethod = headers.getAccessControlRequestMethod();
                HttpHeaders responseHeaders = response.getHeaders();
                responseHeaders.setAccessControlAllowOrigin(headers.getOrigin());
                responseHeaders.setAccessControlAllowMethods(Collections.singletonList(requestMethod));
                responseHeaders.setAccessControlMaxAge(3600);
                responseHeaders.setAccessControlAllowHeaders(headers.getAccessControlRequestHeaders());
                if (requestMethod != null) {
                    responseHeaders.setAccessControlAllowCredentials(true);
                }
                if (request.getMethod() == HttpMethod.OPTIONS) {
                    response.setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                }
            }
            return chain.filter(exchange);
        };
    }
}