package br.com.api.conf;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.web.server.ServerWebExchange;

import java.util.Map;

public class TokenExtract {


    public static final String USER_DETAIL = "x-user-detail";

    public static boolean hasAuthorized(ServerWebExchange exchange) {
        return exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION);
    }


    public static ServerWebExchange populatedUserIdentify(ServerWebExchange exchange, ReactiveJwtDecoder decoder, ObjectMapper mapper) {


        Map<String, Object> claims = decoder.decode(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION).substring(7).trim()).share().block().getClaims();

        try {
            String json = mapper.writeValueAsString(claims);
            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header(USER_DETAIL,json).build();
            exchange.mutate().request(mutatedRequest).build();
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }

        return exchange;
    }
}
