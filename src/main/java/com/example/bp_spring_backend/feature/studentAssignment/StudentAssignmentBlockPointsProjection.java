package com.example.bp_spring_backend.feature.studentAssignment;

public interface StudentAssignmentBlockPointsProjection {

    Integer getStudentId();
    String getStudentFullName();
    Integer getAisId();
    Integer getBlockId();
    Double getBlockPoints();
}
