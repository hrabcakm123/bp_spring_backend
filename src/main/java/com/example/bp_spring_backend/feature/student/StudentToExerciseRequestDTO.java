package com.example.bp_spring_backend.feature.student;

import com.example.bp_spring_backend.core.validation.OnCreate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentToExerciseRequestDTO {

    @NotNull(groups = OnCreate.class, message = "exerciseId is required")
    private Integer exerciseId;
    @NotEmpty(groups = OnCreate.class, message = "List name is wrong or missing.")
    @Valid
    private List<StudentRequestDTO> students;
}
