package com.example.bp_spring_backend.feature.block;

import com.example.bp_spring_backend.core.exception.CustomValidationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlockService {

    private final BlockRepository blockRepository;
    private final BlockMapper blockMapper;
    private static final Logger log = LoggerFactory.getLogger(BlockService.class);

    public BlockEntity getBlockEntityById(Integer id) {
        return blockRepository.findById(id)
                .orElseThrow(() -> new BlockNotFoundException(""));
    }

    public List<BlockEntity> getBlockEntitiesByIds(List<Integer> ids) {
        return blockRepository.findAllById(ids);
    }

    public List<BlockResponseDTO> getBlocksByCriteria(Integer id, Sort sort) {
        Specification<BlockEntity> spec = (root, query, builder) -> null;
        if (id != null) {
            spec = spec.and(BlockSpecification.hasId(id));
        }

        return blockRepository.findAll(spec, sort).stream()
                .map(blockMapper::toDTO)
                .toList();
    }

    public void addBlocks(List<BlockRequestDTO> request) {
        if (request == null) {
            throw new CustomValidationException("List name is wrong or missing.");
        }
        log.info("Adding {} blocks", request.size());
        List<BlockEntity> blocks = request.stream()
                .map(blockMapper::toEntity)
                .toList();

        for (BlockEntity block : blocks) {
            validatePoints(block.getRequiredPoints(), block.getMaxPoints());
        }

        blockRepository.saveAll(blocks);
        log.info("Saved blocks to DB");
    }

    public void deleteBlockById(Integer id) {
        blockRepository.findById(id)
                .orElseThrow(() -> new BlockNotFoundException(""));
        blockRepository.deleteById(id);
    }

    public void updateBlockById(Integer id, BlockRequestDTO request) {
        log.info("Updating block id {}", id);
        BlockEntity block = blockRepository.findById(id)
                .orElseThrow(() -> new BlockNotFoundException(""));

        if (request.getName() != null) {
            block.setName(request.getName());
        }
        if (request.getMaxPoints() != null) {
            block.setMaxPoints(request.getMaxPoints());
        }
        if (request.getRequiredPoints() != null) {
            block.setRequiredPoints(request.getRequiredPoints());
        }

        validatePoints(block.getRequiredPoints(), block.getMaxPoints());

        blockRepository.save(block);
        log.info("Saved block entity");
    }

    private void validatePoints(Double required, Double max) {
        if (required != null && max != null && required > max) {
            throw new CustomValidationException("requiredPoints must be less than or equal to maxPoints.");
        }
    }
}
