package com.example.bp_spring_backend.domains.inputDTO;

import com.example.bp_spring_backend.validation.OnCreate;
import com.example.bp_spring_backend.validation.OnUpdate;
import jakarta.validation.constraints.NotBlank;
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
public class AssignmentRequestDTO {

    @NotNull(groups = OnCreate.class, message = "blockId is required")
    private Integer blockId;
    @NotBlank(groups = OnCreate.class, message = "name is required")
    private String name;
    @NotNull(groups = OnCreate.class, message = "maxPoints is required")
    @Positive(groups = {OnCreate.class, OnUpdate.class}, message = "maxPoints must be positive")
    private Double maxPoints;
}
