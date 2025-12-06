package com.example.bp_spring_backend.domains.outputDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentAssignmentResponseDTO {

    private Integer id;
    private AssignmentResponseDTO assignment;
    private StudentResponseDTO student;
    private Double earnedPoints;
    private String note;
    private UserResponseDTO createdBy;
    private LocalDateTime createdAt;
    private UserResponseDTO updatedBy;
    private LocalDateTime updatedAt;
}
