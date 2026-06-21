package com.example.bp_spring_backend.feature.student;

import com.example.bp_spring_backend.core.validation.OnCreate;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentRequestDTO {

    @NotNull(groups = OnCreate.class, message = "aisId is required")
    private Integer aisId;
    @NotBlank(groups = OnCreate.class, message = "fullName is required")
    private String fullName;
}
