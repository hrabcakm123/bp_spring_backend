package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.AssignmentEntity;
import com.example.bp_spring_backend.domains.entity.BlockEntity;
import com.example.bp_spring_backend.domains.inputDTO.AssignmentRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.AssignmentResponseDTO;
import com.example.bp_spring_backend.exception.AssignmentNotFoundException;
import com.example.bp_spring_backend.exception.CustomValidationException;
import com.example.bp_spring_backend.mapper.AssignmentMapper;
import com.example.bp_spring_backend.repository.AssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final AssignmentMapper assignmentMapper;
    private final BlockService blockService;

    public List<AssignmentResponseDTO> getAllAssignments() {
        List<AssignmentEntity> assignments = assignmentRepository.findAll();
        return assignments.stream()
                .map(assignmentMapper::toDTO)
                .toList();
    }

    public AssignmentResponseDTO getAssignmentById(Integer id) {
        AssignmentEntity assignment = assignmentRepository.findById(id).orElseThrow(() -> new AssignmentNotFoundException(""));
        return assignmentMapper.toDTO(assignment);
    }

    public AssignmentEntity getAssignmentEntityById(Integer id) {
        return assignmentRepository.findById(id).orElseThrow(() -> new AssignmentNotFoundException(""));
    }

    public List<AssignmentResponseDTO> addAssignments(List<AssignmentRequestDTO> request) {
        if (request == null) {
            throw new CustomValidationException("List name is wrong or missing.");
        }
        List<AssignmentEntity> assignments = request.stream()
                .map(dto -> {
                    BlockEntity blockEntity = blockService.getBlockEntityById(dto.getBlockId());
                    return assignmentMapper.toEntity(dto, blockEntity);
                })
                .toList();

        List<AssignmentEntity> savedAssignments = assignmentRepository.saveAll(assignments);

        return savedAssignments.stream()
                .map(assignmentMapper::toDTO)
                .toList();
    }

    public AssignmentResponseDTO deleteAssignmentById(Integer id) {
        AssignmentEntity assignment = assignmentRepository.findById(id).orElseThrow(() -> new AssignmentNotFoundException(""));
        assignmentRepository.deleteById(id);
        return assignmentMapper.toDTO(assignment);
    }

    public AssignmentResponseDTO updateAssignmentById(Integer id, AssignmentRequestDTO request) {
        AssignmentEntity assignment = assignmentRepository.findById(id).orElseThrow(() -> new AssignmentNotFoundException(""));

        if (request.getBlockId() != null) {
            assignment.setBlockEntity(blockService.getBlockEntityById(request.getBlockId()));
        }
        if (request.getName() != null) {
            assignment.setName(request.getName());
        }
        if (request.getNote() != null) {
            assignment.setNote(request.getNote());
        }
        if (request.getMaxPoints() != null) {
            assignment.setMaxPoints(request.getMaxPoints());
        }

        assignment = assignmentRepository.save(assignment);

        return assignmentMapper.toDTO(assignment);
    }
}
