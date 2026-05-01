package com.example.bp_spring_backend.feature.block;

import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class BlockRequestDTOList {

    @Valid
    private List<BlockRequestDTO> blocks;
}
