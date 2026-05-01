package com.example.bp_spring_backend.feature.studentAttendance;

public class StudentAttendanceNotFoundException extends RuntimeException {
    public StudentAttendanceNotFoundException(String message) {
        super(message);
    }
}
