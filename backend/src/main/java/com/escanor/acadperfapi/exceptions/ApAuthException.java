package com.escanor.acadperfapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class ApAuthException extends RuntimeException {
    public ApAuthException(String message) {
        super(message);
    }
}
