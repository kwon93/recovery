package com.blog.recovery.exception;

public class AlreadyExistEmailException extends AllTimeException{

    private static final String MESSAGE = "이미 가입된 이메일 입니다.";

    public AlreadyExistEmailException() {
        super(MESSAGE);
    }

    @Override
    public int statusCode() {
        return 0;
    }
}
