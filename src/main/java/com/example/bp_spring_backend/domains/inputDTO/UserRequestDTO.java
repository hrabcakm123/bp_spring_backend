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

    @NotBlank(groups = OnCreate.class, message = "firstname is required") // cannot be null or blank
    @Pattern(groups = OnUpdate.class, regexp = "^\\s*\\S.*$", message = "firstname must not be blank") // can be null
    private String firstname;
    @NotBlank(groups = OnCreate.class, message = "lastname is required")
    @Pattern(groups = OnUpdate.class, regexp = "^\\s*\\S.*$", message = "lastname must not be blank")
    private String lastname;
    @Email(groups = {OnCreate.class, OnUpdate.class}, message = "email must be valid email address")
    @NotBlank(groups = OnCreate.class, message = "email is required")
    @Pattern(groups = OnUpdate.class, regexp = "^\\s*\\S.*$", message = "email must not be blank")
    private String email;
    @NotBlank(groups = OnCreate.class, message = "password is required")
    @Pattern(groups = OnUpdate.class, regexp = "^\\s*\\S.*$", message = "password must not be blank")
    private String password;
    @NotNull(groups = OnCreate.class, message = "roleEnum is required")
    private RoleEnum roleEnum;
}
