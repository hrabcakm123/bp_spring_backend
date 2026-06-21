package com.example.bp_spring_backend.feature.studentAssignment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentAssignmentBlockPointsResponseDTO {

    private Integer blockId;
    private Double blockPoints;
}
