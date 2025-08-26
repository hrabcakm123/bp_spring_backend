package com.example.bp_spring_backend.domains.outputDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssignmentResponseDTO {

    private Integer id;
    private BlockResponseDTO block;
    private String name;
    private String note; // can be null
    private Double maxPoints;
}
