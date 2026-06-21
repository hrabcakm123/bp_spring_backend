package com.example.bp_spring_backend.feature.block;

public class BlockNotFoundException extends RuntimeException {
    public BlockNotFoundException(String message) {
        super(message);
    }
}
