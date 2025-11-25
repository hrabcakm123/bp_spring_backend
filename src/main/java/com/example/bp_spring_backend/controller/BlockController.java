package com.example.bp_spring_backend.controller;

import com.example.bp_spring_backend.domains.inputDTO.BlockRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.BlockResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.SuccessResponseDTO;
import com.example.bp_spring_backend.service.BlockManagerService;
import com.example.bp_spring_backend.service.BlockService;
import com.example.bp_spring_backend.utils.ResponseFactory;
import com.example.bp_spring_backend.validation.BlockRequestDTOList;
import com.example.bp_spring_backend.validation.OnCreate;
import com.example.bp_spring_backend.validation.OnUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/block")
@RequiredArgsConstructor
public class BlockController {

    private final BlockService blockService;
    private final ResponseFactory responseFactory;
    private final BlockManagerService blockManagerService;

    @GetMapping
    public ResponseEntity<List<BlockResponseDTO>> getBlocksByCriteria(
            @RequestParam(name = "id", required = false) Integer id,
            Sort sort
    ) {
        return ResponseEntity.ok(
                blockService.getBlocksByCriteria(id, sort)
        );
    }

    @PostMapping
    public ResponseEntity<SuccessResponseDTO<List<BlockResponseDTO>>> addBlocks(
            @Validated(OnCreate.class) @RequestBody BlockRequestDTOList request
    ) {
        return responseFactory.created(
                "Blocks created successfully.",
                blockService.addBlocks(request.getBlocks())
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<BlockResponseDTO>> updateBlockById(
            @PathVariable Integer id,
            @Validated(OnUpdate.class) @RequestBody BlockRequestDTO request
    ) {
        return responseFactory.ok(
                "Block updated successfully.",
                blockService.updateBlockById(id, request)
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<BlockResponseDTO>> deleteBlockById(
            @PathVariable Integer id
    ) {
        return responseFactory.ok(
                "Block deleted successfully.",
                blockManagerService.softDeleteBlockCascade(id)
        );
    }
}
