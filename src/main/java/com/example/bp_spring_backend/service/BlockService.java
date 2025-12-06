package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.BlockEntity;
import com.example.bp_spring_backend.domains.inputDTO.BlockRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.BlockResponseDTO;
import com.example.bp_spring_backend.exception.BlockNotFoundException;
import com.example.bp_spring_backend.exception.CustomValidationException;
import com.example.bp_spring_backend.mapper.BlockMapper;
import com.example.bp_spring_backend.repository.BlockRepository;
import com.example.bp_spring_backend.specification.BlockSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlockService {

    private final BlockRepository blockRepository;
    private final BlockMapper blockMapper;

    public BlockEntity getBlockEntityById(Integer id) {
        return blockRepository.findById(id)
                .orElseThrow(() -> new BlockNotFoundException(""));
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

    public List<BlockResponseDTO> addBlocks(List<BlockRequestDTO> request) {
        if (request == null) {
            throw new CustomValidationException("List name is wrong or missing.");
        }
        List<BlockEntity> blocks = request.stream()
                .map(blockMapper::toEntity)
                .toList();

        for (BlockEntity block : blocks) {
            validatePoints(block.getRequiredPoints(), block.getMaxPoints());
        }

        List<BlockEntity> savedBlocks = blockRepository.saveAll(blocks);

        return savedBlocks.stream()
                .map(blockMapper::toDTO)
                .toList();
    }

    public BlockResponseDTO deleteBlockById(Integer id) {
        BlockEntity block = blockRepository.findById(id)
                .orElseThrow(() -> new BlockNotFoundException(""));
        blockRepository.deleteById(id);
        return blockMapper.toDTO(block);
    }

    public BlockResponseDTO updateBlockById(Integer id, BlockRequestDTO request) {
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

        block = blockRepository.save(block);

        return blockMapper.toDTO(block);
    }

    private void validatePoints(Double required, Double max) {
        if (required != null && max != null && required > max) {
            throw new CustomValidationException("requiredPoints must be less than or equal to maxPoints.");
        }
    }
}
