package com.store.backend.service;

public class WorkerException extends RuntimeException {
    public WorkerException(String exception) {
        super(exception);
    }
}
