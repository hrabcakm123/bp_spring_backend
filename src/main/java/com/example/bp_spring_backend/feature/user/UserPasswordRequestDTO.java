package com.example.bp_spring_backend.feature.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPasswordRequestDTO {

    @NotBlank(message = "oldPassword is required")
    private String oldPassword;
    @NotBlank(message = "oldPassword is required")
    private String newPassword;
}
