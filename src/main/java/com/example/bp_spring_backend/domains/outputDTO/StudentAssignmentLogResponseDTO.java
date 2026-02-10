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
    private String studentFullName;
    private Integer studentAisId;
    private String blockName;
    private String assignmentName;
    private Double originalPoints;
    private Double updatedPoints;
    private String originalUserFullName;
    private String updatedByUserFullName;
    private LocalDateTime updatedAt;
}
