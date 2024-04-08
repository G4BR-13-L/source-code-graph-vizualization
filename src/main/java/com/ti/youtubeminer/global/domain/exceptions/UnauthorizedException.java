package com.ti.youtubeminer.global.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends HttpException{
    public UnauthorizedException(String message, HttpStatus status){
        super(message, status);
    }
}
