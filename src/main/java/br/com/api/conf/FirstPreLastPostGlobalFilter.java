package br.com.api.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class FirstPreLastPostGlobalFilter
        implements GlobalFilter, Ordered {


    final Logger logger =
            LoggerFactory.getLogger(FirstPreLastPostGlobalFilter.class);

    @Autowired
    private ReactiveJwtDecoder decoder;

    @Autowired
    private ObjectMapper mapper;



    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {
        logger.info("First Pre Global Filter");


        if (TokenExtract.hasAuthorized(exchange)){
            exchange = TokenExtract.populatedUserIdentify(exchange, decoder, mapper);
        }

        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    logger.info("Last Post Global Filter");
                }));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}