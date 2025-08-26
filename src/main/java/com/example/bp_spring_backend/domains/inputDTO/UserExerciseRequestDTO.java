package com.example.bp_spring_backend.domains.inputDTO;

import com.example.bp_spring_backend.validation.OnCreate;
import com.example.bp_spring_backend.validation.OnUpdate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserExerciseRequestDTO {

    @NotNull(groups = OnCreate.class, message = "userId is required")
    @Positive(groups = OnCreate.class, message = "userId must be positive")
    @Positive(groups = OnUpdate.class, message = "userId must be positive")
    private Integer userId;
    @NotNull(groups = OnCreate.class, message = "exerciseId is required")
    @Positive(groups = OnCreate.class, message = "exerciseId must be positive")
    @Positive(groups = OnUpdate.class, message = "exerciseId must be positive")
    private Integer exerciseId;
}
