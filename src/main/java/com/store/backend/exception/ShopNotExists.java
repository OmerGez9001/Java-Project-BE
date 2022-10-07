package com.store.backend.exception;

public class ShopNotExists extends RuntimeException {
    public ShopNotExists(String message) {
        super(message);
    }
}
