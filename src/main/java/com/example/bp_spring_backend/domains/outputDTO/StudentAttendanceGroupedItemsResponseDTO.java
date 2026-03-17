package com.example.bp_spring_backend.domains.outputDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentAttendanceGroupedItemsResponseDTO {

    private String studentFullName;
    private Integer aisId;
    private List<StudentAssignmentBlockPointsResponseDTO> allBlockPoints;
    private List<StudentAttendanceItemResponseDTO> studentAttendances;
}
