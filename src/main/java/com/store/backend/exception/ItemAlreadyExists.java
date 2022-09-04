package com.store.backend.exception;

public class ItemAlreadyExists extends Exception{
    public ItemAlreadyExists(String item) {
        super("Item already exists: " + item);
    }
}
