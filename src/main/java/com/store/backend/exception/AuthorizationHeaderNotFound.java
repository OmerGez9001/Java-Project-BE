package com.store.backend.exception;

public class AuthorizationHeaderNotFound extends RuntimeException {
    public AuthorizationHeaderNotFound(String servletPath) {
        super(servletPath);
    }
}
