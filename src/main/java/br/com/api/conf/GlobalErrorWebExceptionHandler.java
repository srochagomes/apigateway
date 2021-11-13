package br.com.api.conf;


import br.com.api.conf.exceptions.CustomizeBusinessException;
import br.com.api.conf.exceptions.ThrowableExceptionCustom;
import br.com.rd.customlibs.newrelic.config.UniqueTrackingNumberFilter;
import br.com.api.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@Order(-2)
public class GlobalErrorWebExceptionHandler implements ErrorWebExceptionHandler, AccessDeniedHandler, ServerAuthenticationFailureHandler, ServerAccessDeniedHandler, ServerAuthenticationEntryPoint {

    @Autowired
    private DataBufferWriter bufferWriter;

    public static final String X_RD_TRACEID = "X-rd-traceid";

    @Autowired
    private List<CustomizeBusinessException> exceptions;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        BusinessException error = null;

        CustomizeBusinessException customizeBusinessException =
                exceptions.stream().filter(err -> err.isType(ex)).findFirst().orElseGet(ThrowableExceptionCustom::new);

        customizeBusinessException.setStatusCode(exchange.getResponse().getStatusCode());
        error = customizeBusinessException.build(ex,exchange.getRequest().getURI().toString());

        log.error(ex.getMessage(), ex);


        if (exchange.getResponse().isCommitted()) {
            return Mono.error(error);
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(X_RD_TRACEID,this.getTraceID());
        exchange.getResponse().setStatusCode(error.getHttpStatusCode());
        exchange.getResponse().getHeaders().add(X_RD_TRACEID, this.getTraceID());
        exchange.getResponse().getHeaders().add("Content-Type", "application/json;charset=UTF-8");

        return bufferWriter.write(exchange.getResponse(), error.getOnlyBody());
    }

    private String getTraceID() {
        String traceId = Optional.ofNullable(MDC.get(UniqueTrackingNumberFilter.TRACE_ID_KEY)).orElse("");
        traceId = Optional.ofNullable(traceId.trim().isEmpty()?null:traceId).orElse("not available");

        return traceId;
    }


    @Override
    public Mono<Void> onAuthenticationFailure(
            WebFilterExchange webFilterExchange, AuthenticationException exception) {
        log.warn(exception.getMessage());
        ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().addIfAbsent(HttpHeaders.LOCATION, "/");
        return Mono.error(exception);
    }

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {

    }

    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, AccessDeniedException exception) {
        log.warn(exception.getMessage());
        ServerHttpResponse response = serverWebExchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().addIfAbsent(HttpHeaders.LOCATION, "/");
        return Mono.error(exception);

    }

    @Override
    public Mono<Void> commence(ServerWebExchange serverWebExchange, AuthenticationException exception) {
        log.warn(exception.getMessage());
        ServerHttpResponse response = serverWebExchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().addIfAbsent(HttpHeaders.LOCATION, "/");
        return Mono.error(exception);
    }
}