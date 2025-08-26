package com.example.bp_spring_backend.exception;

public class UserExerciseNotFoundException extends RuntimeException {
    public UserExerciseNotFoundException(String message) {
        super(message);
    }
}
