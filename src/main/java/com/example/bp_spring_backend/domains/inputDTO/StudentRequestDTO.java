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
    @NotBlank(groups = OnCreate.class, message = "firstname is required") // cannot be null and blank
    @Pattern(groups = OnUpdate.class, regexp = "^\\s*\\S.*$", message = "firstname must not be blank") // can be null
    private String firstname;
    @NotBlank(groups = OnCreate.class, message = "lastname is required")
    @Pattern(groups = OnUpdate.class, regexp = "^\\s*\\S.*$", message = "lastname must not be blank")
    private String lastname;
    @Email(groups = {OnCreate.class, OnUpdate.class}, message = "email must be valid email address")
    @NotBlank(groups = OnCreate.class, message = "email is required")
    @Pattern(groups = OnUpdate.class, regexp = "^\\s*\\S.*$", message = "email must not be blank")
    private String email;
}
