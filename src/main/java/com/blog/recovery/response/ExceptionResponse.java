package com.blog.recovery.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public class ExceptionResponse {

    private String code;
    private String errorMessage;
    private Map<String ,String > validation = new HashMap<>();

    @Builder
    public ExceptionResponse(String code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public void addValidation(String field, String defaultMessage) {
        validation.put(field,defaultMessage);
    }
}
