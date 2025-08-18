package com.example.bp_spring_backend.controller;

import com.example.bp_spring_backend.domains.inputDTO.BlockRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.BlockResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.SuccessResponseDTO;
import com.example.bp_spring_backend.service.BlockService;
import com.example.bp_spring_backend.validation.BlockRequestDTOList;
import com.example.bp_spring_backend.validation.OnCreate;
import com.example.bp_spring_backend.validation.OnUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/blocks")
@RequiredArgsConstructor
public class BlockController {

    private final BlockService blockService;

    @GetMapping("/get")
    public ResponseEntity<List<BlockResponseDTO>> getAllBlocks() {
        List<BlockResponseDTO> blocks = blockService.getAllBlocks();
        return ResponseEntity.ok(blocks);
    }

    @GetMapping("/get/id/{id}")
    public ResponseEntity<BlockResponseDTO> getBlockById(@PathVariable Integer id) {
        BlockResponseDTO block = blockService.getBlockById(id);
        return ResponseEntity.ok(block);
    }

    @GetMapping("/get/name/{name}")
    public ResponseEntity<BlockResponseDTO> getBlockByName(@PathVariable String name) {
        BlockResponseDTO block = blockService.getBlockByName(name);
        return ResponseEntity.ok(block);
    }

    @PostMapping("/post")
    public ResponseEntity<SuccessResponseDTO<List<BlockResponseDTO>>> addBlocks(
            @Validated(OnCreate.class) @RequestBody BlockRequestDTOList request
    ) {
        List<BlockResponseDTO> addedBlocks = blockService.addBlocks(request.getBlocks());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponseDTO.<List<BlockResponseDTO>>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Blocks created successfully.")
                        .data(addedBlocks)
                        .build());
    }

    @PatchMapping("/patch/id/{id}")
    public ResponseEntity<SuccessResponseDTO<BlockResponseDTO>> updateBlockById(
            @PathVariable Integer id, @Validated(OnUpdate.class) @RequestBody BlockRequestDTO request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.<BlockResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Block updated successfully.")
                        .data(blockService.updateBlockById(id, request))
                        .build());
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<SuccessResponseDTO<BlockResponseDTO>> deleteBlockById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.<BlockResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Block deleted successfully.")
                        .data(blockService.deleteBlockById(id))
                        .build());
    }
}
