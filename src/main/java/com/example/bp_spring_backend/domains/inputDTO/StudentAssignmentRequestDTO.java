package com.example.bp_spring_backend.domains.inputDTO;

import com.example.bp_spring_backend.validation.OnCreate;
import com.example.bp_spring_backend.validation.OnUpdate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentAssignmentRequestDTO {

    @NotNull(groups = OnCreate.class, message = "assignmentId is required")
    private Integer assignmentId;
    @NotNull(groups = OnCreate.class, message = "studentId is required")
    private Integer studentId;
    @NotNull(groups = OnCreate.class, message = "exerciseSessionId is required")
    private Integer exerciseSessionId;
    @NotNull(groups = OnCreate.class, message = "earnedPoints is required")
    @PositiveOrZero(groups = {OnCreate.class, OnUpdate.class}, message = "earnedPoints must be zero or positive")
    private Double earnedPoints;
}
