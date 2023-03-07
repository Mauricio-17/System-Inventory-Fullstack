package com.mauricio.inventory.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictingRequestException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public ConflictingRequestException(String message){
        super(message);
    }

}
