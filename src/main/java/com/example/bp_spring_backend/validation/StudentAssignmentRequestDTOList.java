package com.example.bp_spring_backend.validation;

import com.example.bp_spring_backend.domains.inputDTO.StudentAssignmentRequestDTO;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class StudentAssignmentRequestDTOList {

    @Valid
    private List<StudentAssignmentRequestDTO> studentAssignments;
}
