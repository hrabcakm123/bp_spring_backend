package com.example.bp_spring_backend.feature.exerciseSession;

import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class ExerciseSessionRequestDTOList {

    @Valid
    private List<ExerciseSessionRequestDTO> ExerciseSessions;
}
