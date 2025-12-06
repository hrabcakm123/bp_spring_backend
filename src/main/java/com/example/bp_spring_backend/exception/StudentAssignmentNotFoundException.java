package com.example.bp_spring_backend.exception;

public class StudentAssignmentNotFoundException extends RuntimeException {
    public StudentAssignmentNotFoundException(String message) {
        super(message);
    }
}
