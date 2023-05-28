package com.api.apigateway.conf.exceptions;

import com.api.apigateway.exception.BusinessException;
import com.api.apigateway.exception.ExceptionResolver;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ResponseStatusExceptionCustom extends CustomizeBusinessException{
    @Override
    public BusinessException build(Throwable err, String ... txt) {
        String path = "";
        if (txt!=null && txt.length>0){
            path = txt[0];
        }
        ResponseStatusException ex = (ResponseStatusException)err;
        return BusinessException.builder()
                .httpStatusCode(HttpStatus.resolve(ex.getStatusCode().value()))
                .message(ex.getMessage())
                .description(ExceptionResolver.getRootException(ex).concat(" path:").concat(path))
                .build();
    }

    @Override
    public Class<?> getType() {
        return ResponseStatusException.class;
    }
}
