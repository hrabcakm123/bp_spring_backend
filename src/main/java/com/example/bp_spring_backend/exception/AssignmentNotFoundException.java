package com.example.bp_spring_backend.exception;

public class AssignmentNotFoundException extends RuntimeException {
    public AssignmentNotFoundException(String message) {
        super(message);
    }
}
