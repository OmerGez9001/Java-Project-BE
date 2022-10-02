package com.store.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class NewTokenWasProvided extends RuntimeException {
    public NewTokenWasProvided(String user) {
        super("new token was already provided for user " + user);
    }
}
