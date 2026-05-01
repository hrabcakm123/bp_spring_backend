package com.example.bp_spring_backend.feature.student;

import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public StudentResponseDTO toDTO(StudentEntity studentEntity) {
        return StudentResponseDTO.builder()
                .id(studentEntity.getId())
                .aisId(studentEntity.getAisId())
                .fullName(studentEntity.getFullName())
                .build();
    }

    public StudentEntity toEntity(StudentRequestDTO request) {
        return StudentEntity.builder()
                .aisId(request.getAisId())
                .fullName(request.getFullName())
                .build();
    }
}
