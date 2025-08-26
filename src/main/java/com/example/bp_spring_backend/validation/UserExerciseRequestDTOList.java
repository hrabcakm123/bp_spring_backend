package com.example.bp_spring_backend.validation;

import com.example.bp_spring_backend.domains.inputDTO.UserExerciseRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class UserExerciseRequestDTOList {

    @NotEmpty(message = "userExercise list must not be empty")
    @Valid
    private List<UserExerciseRequestDTO> userExercises;
}
