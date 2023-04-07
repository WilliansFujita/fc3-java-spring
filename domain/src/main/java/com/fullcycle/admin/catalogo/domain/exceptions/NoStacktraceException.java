package com.fullcycle.admin.catalogo.domain.exceptions;

public class NoStacktraceException extends RuntimeException{

    public NoStacktraceException(final String aMessage){
        this(aMessage,null);
    }

    public NoStacktraceException(final String message, final Throwable cause){
        super(message,cause,true,false);
    }
}
