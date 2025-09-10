package com.example.bp_spring_backend.domains.inputDTO;

import com.example.bp_spring_backend.validation.OnCreate;
import com.example.bp_spring_backend.validation.OnUpdate;
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
    @NotBlank(groups = OnCreate.class, message = "firstname is required")
    private String firstname;
    @NotBlank(groups = OnCreate.class, message = "lastname is required")
    private String lastname;
    @NotBlank(groups = OnCreate.class, message = "email is required")
    @Email(groups = {OnCreate.class, OnUpdate.class}, message = "email must be valid email address")
    private String email;
}
