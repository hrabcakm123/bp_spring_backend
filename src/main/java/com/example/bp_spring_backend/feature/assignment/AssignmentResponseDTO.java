package com.example.bp_spring_backend.feature.assignment;

import com.example.bp_spring_backend.feature.block.BlockResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentResponseDTO {

    private Integer id;
    private BlockResponseDTO block;
    private String name;
    private Double maxPoints;
}
