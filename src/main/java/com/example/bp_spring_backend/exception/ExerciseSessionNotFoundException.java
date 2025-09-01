package com.example.bp_spring_backend.exception;

public class ExerciseSessionNotFoundException extends RuntimeException {
    public ExerciseSessionNotFoundException(String message) {
        super(message);
    }
}
