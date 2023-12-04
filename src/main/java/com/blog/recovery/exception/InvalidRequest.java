package com.blog.recovery.exception;

import lombok.Getter;

@Getter
public class InvalidRequest extends AllTimeException{

    private static final String INVALID_MESSAGE = "바보라고 하지마세요.";

    private  String fieldName;
    private  String message;


    public InvalidRequest(String fieldName, String message) {
        super(INVALID_MESSAGE);
        addValidation(fieldName, message);
    }

    public InvalidRequest(Throwable cause) {
        super(INVALID_MESSAGE, cause);
    }

    @Override
    public int statusCode() {
        return 400;
    }


}
