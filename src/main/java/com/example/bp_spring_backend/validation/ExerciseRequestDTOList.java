package com.example.bp_spring_backend.validation;

import com.example.bp_spring_backend.domains.inputDTO.ExerciseRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ExerciseRequestDTOList {

    @NotEmpty(message = "Exercise list must not be empty")
    @Valid
    private List<ExerciseRequestDTO> exercises;
}
