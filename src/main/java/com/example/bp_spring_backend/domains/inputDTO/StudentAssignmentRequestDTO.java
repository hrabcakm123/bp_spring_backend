package com.example.bp_spring_backend.domains.inputDTO;

import com.example.bp_spring_backend.validation.OnCreate;
import com.example.bp_spring_backend.validation.OnUpdate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    @Positive(groups = OnCreate.class, message = "assignmentId must be positive")
    @Positive(groups = OnUpdate.class, message = "assignmentId must be positive")
    private Integer assignmentId;
    @NotNull(groups = OnCreate.class, message = "studentId is required")
    @Positive(groups = OnCreate.class, message = "studentId must be positive")
    @Positive(groups = OnUpdate.class, message = "studentId must be positive")
    private Integer studentId;
    @NotNull(groups = OnCreate.class, message = "exerciseSessionId is required")
    @Positive(groups = OnCreate.class, message = "exerciseSessionId must be positive")
    @Positive(groups = OnUpdate.class, message = "exerciseSessionId must be positive")
    private Integer exerciseSessionId;
    @NotNull(groups = OnCreate.class, message = "earnedPoints is required")
    @PositiveOrZero(groups = OnCreate.class, message = "earnedPoints must be zero or positive")
    @PositiveOrZero(groups = OnUpdate.class, message = "earnedPoints must be zero or positive")
    private Double earnedPoints;
}
