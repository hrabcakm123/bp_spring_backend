package com.example.bp_spring_backend.validation;

import com.example.bp_spring_backend.domains.inputDTO.UserRequestDTO;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class UserRequestDTOList {

    @Valid
    private List<UserRequestDTO> users;
}
