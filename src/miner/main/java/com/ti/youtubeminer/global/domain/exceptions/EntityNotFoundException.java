package com.ti.youtubeminer.global.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends HttpException {
    public EntityNotFoundException(String message, HttpStatus status) {
        super(message, status);
    }
}