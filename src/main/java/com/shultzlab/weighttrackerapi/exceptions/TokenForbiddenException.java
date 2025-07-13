package com.shultzlab.weighttrackerapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class TokenForbiddenException extends Exception {
    private static final long serialVersionUID = 2L;

    public TokenForbiddenException(String s){
        super("Resource does not belong to this user!");
    }
}
