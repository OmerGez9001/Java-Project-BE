package com.store.backend.exception;

public class CustomerAlreadyExists extends Exception {

    public CustomerAlreadyExists(String message) {
        super(message);
    }
}
