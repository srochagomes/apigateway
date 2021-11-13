package br.com.api.conf.exceptions;

import br.com.api.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
public class AccessDeniedExceptionCustom extends CustomizeBusinessException{
    @Override
    public BusinessException build(Throwable err, String ... txt) {
        AccessDeniedException ex = (AccessDeniedException)err;
        return BusinessException.builder()
                .httpStatusCode(HttpStatus.UNAUTHORIZED)
                .message("Access denied.")
                .description("The operation is not valid for role.")
                .build();
    }

    @Override
    public Class<?> getType() {
        return AccessDeniedException.class;
    }
}
