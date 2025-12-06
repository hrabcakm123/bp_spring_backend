package com.example.bp_spring_backend.domains.outputDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentAssignmentItemResponseDTO {

    private Integer studentAssignmentId;
    private Double earnedPoints;
    private String note;
}
