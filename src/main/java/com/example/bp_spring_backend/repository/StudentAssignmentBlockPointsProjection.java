package com.example.bp_spring_backend.repository;

public interface StudentAssignmentBlockPointsProjection {

    Integer getStudentId();
    String getStudentFullName();
    Integer getAisId();
    Integer getBlockId();
    Double getBlockPoints();
}
