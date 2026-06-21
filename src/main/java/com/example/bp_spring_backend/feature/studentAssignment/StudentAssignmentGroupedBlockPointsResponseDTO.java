package com.example.bp_spring_backend.feature.studentAssignment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentAssignmentGroupedBlockPointsResponseDTO {

    private String studentFullName;
    private Integer aisId;
    private List<StudentAssignmentBlockPointsResponseDTO> allBlockPoints;
}
