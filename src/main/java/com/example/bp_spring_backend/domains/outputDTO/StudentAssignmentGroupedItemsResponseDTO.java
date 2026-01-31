package com.example.bp_spring_backend.domains.outputDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentAssignmentGroupedItemsResponseDTO {

    private String studentFullName;
    private Integer aisId;
    private List<StudentAssignmentItemResponseDTO> studentAssignments;
}
