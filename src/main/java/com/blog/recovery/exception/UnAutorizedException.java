package com.blog.recovery.exception;

public class UnAutorizedException extends AllTimeException{

    private static final String MESSAGE = "인증이 필요합니다.";

    public UnAutorizedException() {
        super(MESSAGE);
    }
    @Override
    public int statusCode() {
        return 401;
    }
}
