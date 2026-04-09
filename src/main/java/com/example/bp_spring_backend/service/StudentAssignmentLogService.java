package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.StudentAssignmentEntity;
import com.example.bp_spring_backend.domains.entity.StudentAssignmentLogEntity;
import com.example.bp_spring_backend.domains.entity.UserEntity;
import com.example.bp_spring_backend.domains.outputDTO.PageResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.StudentAssignmentLogResponseDTO;
import com.example.bp_spring_backend.exception.StudentAssignmentLogNotFoundException;
import com.example.bp_spring_backend.mapper.PageMapper;
import com.example.bp_spring_backend.mapper.StudentAssignmentLogMapper;
import com.example.bp_spring_backend.repository.StudentAssignmentLogRepository;
import com.example.bp_spring_backend.specification.StudentAssignmentLogSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StudentAssignmentLogService {

    private final StudentAssignmentLogRepository studentAssignmentLogRepository;
    private final StudentAssignmentLogMapper studentAssignmentLogMapper;

    public void createLog(StudentAssignmentEntity studentAssignment, Double oldPoints, Double newPoints, UserEntity updatedBy) {
        StudentAssignmentLogEntity log = StudentAssignmentLogEntity.builder()
                .studentAssignment(studentAssignment)
                .originalPoints(oldPoints)
                .updatedPoints(newPoints)
                .originalUser(Objects.requireNonNullElse(studentAssignment.getUpdatedBy(), studentAssignment.getCreatedBy()))
                .updatedByUser(updatedBy)
                .updatedAt(LocalDateTime.now())
                .build();

        studentAssignmentLogRepository.save(log);
    }

    public PageResponseDTO<StudentAssignmentLogResponseDTO> getStudentAssignmentLogsByCriteria(
            String originalUserFullName,
            String updatedByUserFullName,
            Pageable pageable
    ) {
        Specification<StudentAssignmentLogEntity> spec = (root, query, builder) -> null;

        if (originalUserFullName != null && !originalUserFullName.isBlank()) {
            spec = spec.and(StudentAssignmentLogSpecification.containsOriginalUserFullName(originalUserFullName));
        }

        if (updatedByUserFullName != null && !updatedByUserFullName.isBlank()) {
            spec = spec.and(StudentAssignmentLogSpecification.containsUpdatedByUserFullName(updatedByUserFullName));
        }

        return PageMapper.toResponse(
                studentAssignmentLogRepository.findAll(spec, pageable),
                studentAssignmentLogMapper::toDTO
        );
    }

    public void deleteStudentAssignmentLogById(Integer id) {
        studentAssignmentLogRepository.findById(id)
                .orElseThrow(() -> new StudentAssignmentLogNotFoundException(""));
        studentAssignmentLogRepository.deleteById(id);
    }

    public void softDeleteStudentAssignmentLogsByUserId(Integer userId) {
        studentAssignmentLogRepository.softDeleteByUserId(userId);
    }

    public void softDeleteStudentAssignmentLogsByStudentAssignmentId(Integer studentAssignmentId) {
        studentAssignmentLogRepository.softDeleteByStudentAssignmentId(studentAssignmentId);
    }

    public void softDeleteStudentAssignmentLogsByStudentAssignmentIds(List<Integer> studentAssignmentIds) {
        if (studentAssignmentIds.isEmpty()) return;
        studentAssignmentLogRepository.softDeleteByStudentAssignmentIds(studentAssignmentIds);
    }
}
