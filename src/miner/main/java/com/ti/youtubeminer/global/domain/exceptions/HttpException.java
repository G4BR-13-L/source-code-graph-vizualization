package com.ti.youtubeminer.global.domain.exceptions;

import org.springframework.http.HttpStatus;


public class HttpException extends RuntimeException{

    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public HttpException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
