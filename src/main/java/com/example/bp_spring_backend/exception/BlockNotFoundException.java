package com.example.bp_spring_backend.exception;

public class BlockNotFoundException extends RuntimeException {
    public BlockNotFoundException(String message) {
        super(message);
    }
}
