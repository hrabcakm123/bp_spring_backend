package com.example.bp_spring_backend.exception;

public class StudentAttendanceNotFoundException extends RuntimeException {
    public StudentAttendanceNotFoundException(String message) {
        super(message);
    }
}
