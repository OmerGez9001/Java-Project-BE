package com.store.backend.exception;

public class ItemAlreadyExists extends RuntimeException{
    public ItemAlreadyExists(String item) {
        super("Item already exists: " + item);
    }
}
