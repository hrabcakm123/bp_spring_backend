package com.example.bp_spring_backend.domains.inputDTO;

import com.example.bp_spring_backend.validation.OnCreate;
import com.example.bp_spring_backend.validation.OnUpdate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseSessionRequestDTO {

    @NotNull(groups = OnCreate.class, message = "exerciseId is required")
    @Positive(groups = OnCreate.class, message = "exerciseId must be positive")
    @Positive(groups = OnUpdate.class, message = "exerciseId must be positive")
    private Integer exerciseId;
    @NotNull(groups = OnCreate.class, message = "sessionDate is required")
    @PastOrPresent(groups = OnCreate.class, message = "sessionDate must be in the past or today")
    @PastOrPresent(groups = OnUpdate.class, message = "sessionDate must be in the past or today")
    private LocalDate sessionDate;
}
