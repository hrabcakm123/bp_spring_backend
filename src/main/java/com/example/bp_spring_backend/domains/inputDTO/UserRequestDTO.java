package com.example.bp_spring_backend.domains.inputDTO;

import com.example.bp_spring_backend.domains.enums.RoleEnum;
import com.example.bp_spring_backend.validation.OnCreate;
import com.example.bp_spring_backend.validation.OnUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {

    @NotBlank(groups = OnCreate.class, message = "fullName is required")
    private String fullName;
    @NotBlank(groups = OnCreate.class, message = "email is required")
    @Email(groups = {OnCreate.class, OnUpdate.class}, message = "email must be valid email address")
    private String email;
    @NotBlank(groups = OnCreate.class, message = "password is required")
    private String password;
    @NotNull(groups = OnCreate.class, message = "roleEnum is required")
    private RoleEnum roleEnum;
}
