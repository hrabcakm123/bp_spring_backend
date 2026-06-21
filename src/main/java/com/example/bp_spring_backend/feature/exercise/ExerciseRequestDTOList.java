package com.example.bp_spring_backend.feature.exercise;

import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class ExerciseRequestDTOList {

    @Valid
    private List<ExerciseRequestDTO> exercises;
}
