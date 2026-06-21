package com.example.bp_spring_backend.feature.block;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlockResponseDTO {

    private Integer id;
    private String name;
    private Double maxPoints;
    private Double requiredPoints;
}
