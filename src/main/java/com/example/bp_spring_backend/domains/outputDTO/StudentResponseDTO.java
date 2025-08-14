package com.example.bp_spring_backend.domains.outputDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponseDTO {

    private Integer id;
    private Integer aisId;
    private String firstname;
    private String lastname;
    private String email;
}
