package com.example.bp_spring_backend.feature.exerciseSession;

import com.example.bp_spring_backend.core.validation.OnCreate;
import com.example.bp_spring_backend.core.validation.OnUpdate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
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
    private Integer exerciseId;
    @NotNull(groups = OnCreate.class, message = "sessionDate is required")
    @PastOrPresent(groups = {OnCreate.class, OnUpdate.class}, message = "sessionDate must be in the past or today")
    private LocalDate sessionDate;
}
