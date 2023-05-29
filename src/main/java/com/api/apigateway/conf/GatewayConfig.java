package com.api.apigateway.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;

//@Configuration
public class GatewayConfig {

  //  @Bean
    public WebFilter corsFilter() {
        return (ServerWebExchange exchange, WebFilterChain chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (CorsUtils.isCorsRequest(request)) {
                ServerHttpResponse response = exchange.getResponse();
                response.getHeaders().setAccessControlAllowOrigin("*"); // Define o valor do cabeçalho Access-Control-Allow-Origin
                response.getHeaders().setAccessControlAllowCredentials(true); // Define o valor do cabeçalho Access-Control-Allow-Credentials
                response.getHeaders().setAccessControlAllowMethods(Arrays.asList(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE));
                response.getHeaders().setAccessControlMaxAge(Duration.ofHours(1)); // Define o valor do cabeçalho Access-Control-Max-Age
                response.getHeaders().setAccessControlAllowHeaders(Arrays.asList("*")); // Define o valor do cabeçalho Access-Control-Allow-Headers

                if (request.getMethod() == HttpMethod.OPTIONS) {
                    response.setStatusCode(HttpStatus.OK); // Retorna status 200 para as requisições OPTIONS
                    return Mono.empty();
                }
            }
            return chain.filter(exchange);
        };
    }

}