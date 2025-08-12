package com.example.bp_spring_backend.validation;

import com.example.bp_spring_backend.domains.inputDTO.UserRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class UserRequestDTOList {

    @NotEmpty(message = "User list must not be empty")
    @Valid
    private List<UserRequestDTO> users;
}
