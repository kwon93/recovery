package com.blog.recovery.exception;

public class InvalidSignInfomation extends AllTimeException{

    private final static String MESSAGE = "이메일과 비밀번호를 다시 확인해주세요.";

    public InvalidSignInfomation() {
        super(MESSAGE);
    }
    @Override
    public int statusCode() {
        return 400;
    }
}
