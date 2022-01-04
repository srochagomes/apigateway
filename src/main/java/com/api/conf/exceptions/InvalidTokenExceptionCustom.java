package com.api.conf.exceptions;

import com.api.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class InvalidTokenExceptionCustom extends CustomizeBusinessException{
    @Override
    public BusinessException build(Throwable err, String ... txt) {
        InvalidBearerTokenException ex = (InvalidBearerTokenException)err;
        return BusinessException.builder()
                .httpStatusCode(HttpStatus.UNAUTHORIZED)
                .message("Token must be valid for operation.")
                .description(Optional.ofNullable(ex.getMessage()).orElse(ex.toString()))
                .build();
    }

    @Override
    public Class<?> getType() {
        return InvalidBearerTokenException.class;
    }
}
