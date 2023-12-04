package com.blog.recovery.controller;

import com.blog.recovery.exception.AllTimeException;
import com.blog.recovery.exception.InvalidRequest;
import com.blog.recovery.exception.PostNotFound;
import com.blog.recovery.response.ExceptionResponse;
import com.blog.recovery.response.PostResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ExceptionResponse exceptionHandler(MethodArgumentNotValidException e){
            ExceptionResponse response = ExceptionResponse.builder()
                    .code("400")
                    .errorMessage("잘못된 요청입니다.")
                    .build();

        for (FieldError fieldError : e.getFieldErrors() ) {
            response.addValidation(fieldError.getField(),fieldError.getDefaultMessage());
        }

            return response;


    }

    @ResponseBody
    @ExceptionHandler(AllTimeException.class)
    public ResponseEntity<ExceptionResponse> postNotFount(AllTimeException e){

        ExceptionResponse response = ExceptionResponse.builder()
                .code(String.valueOf(e.statusCode()))
                .errorMessage(e.getMessage())
                .validation(e.getValidation())
                .build();

        return ResponseEntity.status(e.statusCode())
                .body(response);
    }

}
