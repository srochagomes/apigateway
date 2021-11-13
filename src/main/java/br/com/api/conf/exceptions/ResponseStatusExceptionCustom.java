package br.com.api.conf.exceptions;

import br.com.api.exception.BusinessException;
import br.com.api.exception.ExceptionResolver;
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
                .httpStatusCode(ex.getStatus())
                .message(ex.getMessage())
                .description(ExceptionResolver.getRootException(ex).concat(" path:").concat(path))
                .build();
    }

    @Override
    public Class<?> getType() {
        return ResponseStatusException.class;
    }
}
