package br.com.api.conf.exceptions;

import br.com.api.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CredentialNotFoundExceptionCustom extends CustomizeBusinessException{
    @Override
    public BusinessException build(Throwable err, String ... txt) {
        AuthenticationCredentialsNotFoundException ex = (AuthenticationCredentialsNotFoundException)err;
        return BusinessException.builder()
                .httpStatusCode(HttpStatus.UNAUTHORIZED)
                .message("Token must be informed for operation.")
                .description(Optional.ofNullable(ex.getMessage()).orElse(ex.toString()))
                .build();
    }

    @Override
    public Class<?> getType() {
        return AuthenticationCredentialsNotFoundException.class;
    }
}
