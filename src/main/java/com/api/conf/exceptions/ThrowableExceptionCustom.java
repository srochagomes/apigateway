package com.api.conf.exceptions;

import com.api.exception.BusinessException;
import com.api.exception.ExceptionResolver;
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
