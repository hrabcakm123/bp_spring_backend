package com.example.bp_spring_backend.feature.user;

import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class UserRequestDTOList {

    @Valid
    private List<UserRequestDTO> users;
}
