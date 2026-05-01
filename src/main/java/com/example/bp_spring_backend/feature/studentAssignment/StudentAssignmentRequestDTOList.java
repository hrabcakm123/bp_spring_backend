package com.example.bp_spring_backend.feature.studentAssignment;

import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class StudentAssignmentRequestDTOList {

    @Valid
    private List<StudentAssignmentRequestDTO> studentAssignments;
}
