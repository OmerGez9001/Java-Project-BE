package com.store.backend.exception;

public class ShopAlreadyExists extends RuntimeException {
    public ShopAlreadyExists(String message) {
        super(message);
    }
}
