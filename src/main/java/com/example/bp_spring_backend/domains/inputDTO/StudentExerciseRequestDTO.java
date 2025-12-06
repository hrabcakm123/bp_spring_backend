package com.example.bp_spring_backend.domains.inputDTO;

import com.example.bp_spring_backend.validation.OnCreate;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentExerciseRequestDTO {

    @NotNull(groups = OnCreate.class, message = "studentId is required")
    private Integer studentId;
    @NotNull(groups = OnCreate.class, message = "exerciseId is required")
    private Integer exerciseId;
}
