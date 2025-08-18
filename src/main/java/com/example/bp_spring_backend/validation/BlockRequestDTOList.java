package com.example.bp_spring_backend.validation;

import com.example.bp_spring_backend.domains.inputDTO.BlockRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class BlockRequestDTOList {

    @NotEmpty(message = "Block list must not be empty")
    @Valid
    private List<BlockRequestDTO> blocks;
}
