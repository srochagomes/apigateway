package com.api.apigateway.conf.exceptions;

import com.api.apigateway.exception.BusinessException;
import org.springframework.http.HttpStatus;

public abstract class CustomizeBusinessException {

    private HttpStatus status;

    public void setStatusCode(HttpStatus s){
        this.status = s;
    }
    public HttpStatus getStatusCode(){
        return this.status;
    }

    public abstract BusinessException build(Throwable ex, String ... txt);

    public abstract Class<?> getType();

    public boolean isType(Object obj){
        return this.getType().isInstance(obj);
    }

}
