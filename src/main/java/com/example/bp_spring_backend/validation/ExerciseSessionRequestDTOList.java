package com.example.bp_spring_backend.validation;

import com.example.bp_spring_backend.domains.inputDTO.ExerciseSessionRequestDTO;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class ExerciseSessionRequestDTOList {

    @Valid
    private List<ExerciseSessionRequestDTO> ExerciseSessions;
}
