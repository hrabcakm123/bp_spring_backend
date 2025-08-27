package com.example.bp_spring_backend.exception;

public class StudentExerciseNotFoundException extends RuntimeException {
    public StudentExerciseNotFoundException(String message) {
        super(message);
    }
}
