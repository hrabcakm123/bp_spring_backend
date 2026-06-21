package com.example.bp_spring_backend.feature.assignment;

import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class AssignmentRequestDTOList {

    @Valid
    private List<AssignmentRequestDTO> assignments;
}
