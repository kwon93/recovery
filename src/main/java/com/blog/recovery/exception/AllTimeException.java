package com.blog.recovery.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class AllTimeException extends RuntimeException {

    private final Map<String ,String > validation = new HashMap<>();

    public AllTimeException(String message) {
        super(message);
    }

    public AllTimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int statusCode();

    public void addValidation(String fieldName, String message){
        validation.put(fieldName,message);
    };
}
