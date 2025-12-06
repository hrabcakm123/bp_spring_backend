package com.example.bp_spring_backend.validation;

import com.example.bp_spring_backend.domains.inputDTO.ExerciseRequestDTO;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class ExerciseRequestDTOList {

    @Valid
    private List<ExerciseRequestDTO> exercises;
}
