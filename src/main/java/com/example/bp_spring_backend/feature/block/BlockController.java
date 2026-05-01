package com.example.bp_spring_backend.feature.block;

import com.example.bp_spring_backend.core.dto.SuccessResponseDTO;
import com.example.bp_spring_backend.core.utils.ResponseFactory;
import com.example.bp_spring_backend.core.validation.OnCreate;
import com.example.bp_spring_backend.core.validation.OnUpdate;
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
    public ResponseEntity<SuccessResponseDTO<Void>> addBlocks(
            @Validated(OnCreate.class) @RequestBody BlockRequestDTOList request
    ) {
        blockService.addBlocks(request.getBlocks());
        return responseFactory.created(
                "Blocks created successfully."
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<Void>> updateBlockById(
            @PathVariable Integer id,
            @Validated(OnUpdate.class) @RequestBody BlockRequestDTO request
    ) {
        blockService.updateBlockById(id, request);
        return responseFactory.ok(
                "Block updated successfully."
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<Void>> deleteBlockById(
            @PathVariable Integer id
    ) {
        blockManagerService.softDeleteBlockCascade(id);
        return responseFactory.ok(
                "Block deleted successfully."
        );
    }
}
