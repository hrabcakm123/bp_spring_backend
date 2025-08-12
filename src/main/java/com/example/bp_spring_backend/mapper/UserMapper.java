package com.example.bp_spring_backend.mapper;

import com.example.bp_spring_backend.domains.entity.UserEntity;
import com.example.bp_spring_backend.domains.inputDTO.UserRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.UserResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDTO toDTO(UserEntity userEntity) {
        return UserResponseDTO.builder()
                .id(userEntity.getId())
                .firstname(userEntity.getFirstname())
                .lastname(userEntity.getLastname())
                .email(userEntity.getEmail())
                .roleEnum(userEntity.getRoleEnum())
                .build();
    }

    public UserEntity toEntity(UserRequestDTO request) {
        return UserEntity.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(request.getPassword())
                .roleEnum(request.getRoleEnum())
                .build();
    }
}
