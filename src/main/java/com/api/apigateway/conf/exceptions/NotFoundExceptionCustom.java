package com.api.apigateway.conf.exceptions;

import com.api.apigateway.exception.BusinessException;
import com.api.apigateway.exception.ExceptionResolver;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.net.ConnectException;
import java.util.Optional;

@Component
public class NotFoundExceptionCustom extends CustomizeBusinessException{
    @Override
    public BusinessException build(Throwable err, String ... txt) {
        ConnectException ex = (ConnectException)err;
        return BusinessException.builder()
                .httpStatusCode(HttpStatus.NOT_FOUND)
                .message(Optional.ofNullable(ex.getMessage()).orElse(ex.toString()))
                .description(ExceptionResolver.getRootException(ex))
                .build();
    }

    @Override
    public Class<?> getType() {
        return NotFoundException.class;
    }
}
