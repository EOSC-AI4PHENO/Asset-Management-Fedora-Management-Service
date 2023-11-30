package com.siseth.management.module.fedora.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ServerException extends RuntimeException{

    public ServerException(String message) {
        super(message);
    }

}
