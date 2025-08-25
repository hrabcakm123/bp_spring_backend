package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.BlockEntity;
import com.example.bp_spring_backend.domains.inputDTO.BlockRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.BlockResponseDTO;
import com.example.bp_spring_backend.exception.BlockNotFoundException;
import com.example.bp_spring_backend.exception.CustomValidationException;
import com.example.bp_spring_backend.mapper.BlockMapper;
import com.example.bp_spring_backend.repository.BlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlockService {

    private final BlockRepository blockRepository;
    private final BlockMapper blockMapper;

    public List<BlockResponseDTO> getAllBlocks() {
        List<BlockEntity> blocks = blockRepository.findAll();
        return blocks.stream()
                .map(blockMapper::toDTO)
                .toList();
    }

    public BlockResponseDTO getBlockById(Integer id) {
        BlockEntity block = blockRepository.findById(id).orElseThrow(() -> new BlockNotFoundException(""));
        return blockMapper.toDTO(block);
    }

    public BlockEntity getBlockEntityById(Integer id) {
        return blockRepository.findById(id).orElseThrow(() -> new BlockNotFoundException(""));
    }

    public BlockResponseDTO getBlockByName(String name) {
        BlockEntity block = blockRepository.findByName(name).orElseThrow(() -> new BlockNotFoundException(""));
        return blockMapper.toDTO(block);
    }

    public List<BlockResponseDTO> addBlocks(List<BlockRequestDTO> request) {
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
        BlockEntity block = blockRepository.findById(id).orElseThrow(() -> new BlockNotFoundException(""));
        blockRepository.deleteById(id);
        return blockMapper.toDTO(block);
    }

    public BlockResponseDTO updateBlockById(Integer id, BlockRequestDTO request) {
        BlockEntity block = blockRepository.findById(id).orElseThrow(() -> new BlockNotFoundException(""));

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
