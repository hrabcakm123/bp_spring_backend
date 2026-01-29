package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.AssignmentEntity;
import com.example.bp_spring_backend.domains.entity.BlockEntity;
import com.example.bp_spring_backend.domains.inputDTO.AssignmentRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.AssignmentResponseDTO;
import com.example.bp_spring_backend.exception.AssignmentNotFoundException;
import com.example.bp_spring_backend.exception.BlockNotFoundException;
import com.example.bp_spring_backend.exception.CustomValidationException;
import com.example.bp_spring_backend.mapper.AssignmentMapper;
import com.example.bp_spring_backend.repository.AssignmentRepository;
import com.example.bp_spring_backend.specification.AssignmentSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final AssignmentMapper assignmentMapper;
    private final BlockService blockService;

    public AssignmentEntity getAssignmentEntityById(Integer id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new AssignmentNotFoundException(""));
    }

    public List<AssignmentEntity> getAssignmentEntitiesByIds(List<Integer> ids) {
        return assignmentRepository.findAllById(ids);
    }

    public List<AssignmentResponseDTO> getAssignmentsByCriteria(Integer id, Integer blockId, Sort sort) {
        Specification<AssignmentEntity> spec = (root, query, builder) -> null;
        if (id != null) {
            spec = spec.and(AssignmentSpecification.hasId(id));
        }
        if (blockId != null) {
            spec = spec.and(AssignmentSpecification.hasBlockId(blockId));
        }

        return assignmentRepository.findAll(spec, sort).stream()
                .map(assignmentMapper::toDTO)
                .toList();
    }

    public List<AssignmentEntity> addAssignmentEntities(List<AssignmentRequestDTO> request) {
        if (request == null) {
            throw new CustomValidationException("List name is wrong or missing.");
        }
        List<Integer> blockIds = request.stream()
                .map(AssignmentRequestDTO::getBlockId)
                .distinct()
                .toList();

        List<BlockEntity> blocks = blockService.getBlockEntitiesByIds(blockIds);
        Map<Integer, BlockEntity> blockMap = blocks.stream()
                .collect(Collectors.toMap(BlockEntity::getId, Function.identity()));

        List<AssignmentEntity> assignments = request.stream()
                .map(dto -> {
                    BlockEntity block = blockMap.get(dto.getBlockId());
                    if (block == null) {
                        throw new BlockNotFoundException("");
                    }
                    return assignmentMapper.toEntity(dto, block);
                })
                .toList();

        return assignmentRepository.saveAll(assignments);
    }

    public AssignmentResponseDTO deleteAssignmentById(Integer id) {
        AssignmentEntity assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new AssignmentNotFoundException(""));
        assignmentRepository.deleteById(id);
        return assignmentMapper.toDTO(assignment);
    }

    public AssignmentResponseDTO updateAssignmentById(Integer id, AssignmentRequestDTO request) {
        AssignmentEntity assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new AssignmentNotFoundException(""));

        if (request.getBlockId() != null) {
            assignment.setBlockEntity(blockService.getBlockEntityById(request.getBlockId()));
        }
        if (request.getName() != null) {
            assignment.setName(request.getName());
        }
        if (request.getMaxPoints() != null) {
            assignment.setMaxPoints(request.getMaxPoints());
        }

        assignment = assignmentRepository.save(assignment);

        return assignmentMapper.toDTO(assignment);
    }

    public List<AssignmentEntity> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    @Transactional
    public List<Integer> softDeleteAssignmentsByBlockId(Integer blockId) {

        List<Integer> assignmentIds = assignmentRepository.findAssignmentIdsByBlockId(blockId);

        if (!assignmentIds.isEmpty()) {
            assignmentRepository.softDeleteByBlockId(blockId);
        }

        return assignmentIds;
    }
}
