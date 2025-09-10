package com.example.bp_spring_backend.validation;

import com.example.bp_spring_backend.domains.inputDTO.BlockRequestDTO;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class BlockRequestDTOList {

    @Valid
    private List<BlockRequestDTO> blocks;
}
