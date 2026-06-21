package com.example.bp_spring_backend.feature.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequestDTO {

    @NotBlank(message = "email is required")
    @Email(message = "email must be valid email address")
    private String email;
    @NotBlank(message = "password is required")
    private String password;
}
