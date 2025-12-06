package com.example.bp_spring_backend.domains.outputDTO;

import com.example.bp_spring_backend.domains.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private Integer id;
    private String fullName;
    private String email;
    private RoleEnum roleEnum;
}
