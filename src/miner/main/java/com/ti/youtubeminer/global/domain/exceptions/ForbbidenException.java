package com.ti.youtubeminer.global.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ForbbidenException extends HttpException{
    public ForbbidenException(String message, HttpStatus httpStatus){
        super(message, httpStatus);
    }
}
