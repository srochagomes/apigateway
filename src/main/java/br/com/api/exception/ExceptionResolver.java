package br.com.api.exception;

import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.Objects;

public class ExceptionResolver {

    private ExceptionResolver() {
        throw new IllegalStateException("Utility class");
    }

    public static String getRootException(Throwable ex){
        String description = "";
        if (ex instanceof BusinessException){
            description = " ["+((BusinessException)ex).getDescription()+"]";
        }

        if (Objects.isNull(ExceptionUtils.getRootCause(ex))){
            return String.format("%s in class: %s Line: %s ", ExceptionUtils.getRootCauseMessage(ex)+description, ex.getClass().getSimpleName(), 0);
        }

        if (Objects.isNull(ExceptionUtils.getRootCause(ex).getStackTrace())|| ExceptionUtils.getRootCause(ex).getStackTrace().length==0){
            return String.format("%s in class: %s Line: %s ", ExceptionUtils.getRootCauseMessage(ex)+description, ex.getClass().getSimpleName(), 0);
        }
        return String.format("%s in class: %s Line: %s", ExceptionUtils.getRootCauseMessage(ex)+description, ExceptionUtils.getRootCause(ex).getStackTrace()[0].getClassName(), ExceptionUtils.getRootCause(ex).getStackTrace()[0].getLineNumber());
    }
}
