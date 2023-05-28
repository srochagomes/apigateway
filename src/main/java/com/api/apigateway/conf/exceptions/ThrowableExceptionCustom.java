package com.api.apigateway.conf.exceptions;

import com.api.apigateway.exception.BusinessException;
import com.api.apigateway.exception.ExceptionResolver;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ThrowableExceptionCustom extends CustomizeBusinessException{


    @Override
    public BusinessException build(Throwable ex, String ... txt) {
        HttpStatus statusCodeReturn = Optional.ofNullable(this.getStatusCode()).orElseGet(() -> HttpStatus.INTERNAL_SERVER_ERROR);
        return BusinessException.builder()
                .httpStatusCode(statusCodeReturn)
                .message(Optional.ofNullable(ex.getMessage()).orElse(ex.toString()))
                .description(ExceptionResolver.getRootException(ex))
                .build();
    }

    @Override
    public Class<?> getType() {
        return Throwable.class;
    }
}
