package com.api.apigateway.conf.exceptions;

import com.api.apigateway.exception.BusinessException;
import com.api.apigateway.exception.ExceptionResolver;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.net.ConnectException;
import java.util.Optional;

@Component
public class ConnectionExceptionCustom extends CustomizeBusinessException{
    @Override
    public BusinessException build(Throwable err, String ... txt) {
        ConnectException ex = (ConnectException)err;
        return BusinessException.builder()
                .httpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(Optional.ofNullable(ex.getMessage()).orElse(ex.toString()))
                .description(ExceptionResolver.getRootException(ex))
                .build();
    }

    @Override
    public Class<?> getType() {
        return ConnectException.class;
    }
}
