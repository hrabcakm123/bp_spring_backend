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
public class StudentAssignmentLogResponseDTO {

    private Integer id;
    private StudentAssignmentResponseDTO studentAssignment;
    private Double originalPoints;
    private Double updatedPoints;
    private UserResponseDTO originalUser;
    private UserResponseDTO updatedByUser;
    private LocalDateTime updatedAt;
}
