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
                .fullName(userEntity.getFullName())
                .email(userEntity.getEmail())
                .roleEnum(userEntity.getRoleEnum())
                .build();
    }

    public UserEntity toEntity(UserRequestDTO request, String password) {
        return UserEntity.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(password)
                .roleEnum(request.getRoleEnum())
                .build();
    }
}
