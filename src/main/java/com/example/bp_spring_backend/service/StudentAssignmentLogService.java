package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.StudentAssignmentEntity;
import com.example.bp_spring_backend.domains.entity.StudentAssignmentLogEntity;
import com.example.bp_spring_backend.domains.entity.UserEntity;
import com.example.bp_spring_backend.domains.outputDTO.StudentAssignmentLogResponseDTO;
import com.example.bp_spring_backend.exception.StudentAssignmentLogNotFoundException;
import com.example.bp_spring_backend.mapper.StudentAssignmentLogMapper;
import com.example.bp_spring_backend.repository.StudentAssignmentLogRepository;
import com.example.bp_spring_backend.specification.StudentAssignmentLogSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
                .originalUser(studentAssignment.getUpdatedBy())
                .updatedByUser(updatedBy)
                .updatedAt(LocalDateTime.now())
                .build();

        studentAssignmentLogRepository.save(log);
    }

    public List<StudentAssignmentLogResponseDTO> getStudentAssignmentLogsByCriteria(
            String originalUserFullName,
            String updatedByUserFullName,
            Sort sort
    ) {
        Specification<StudentAssignmentLogEntity> spec = (root, query, builder) -> null;

        if (originalUserFullName != null && !originalUserFullName.isBlank()) {
            spec = spec.and(StudentAssignmentLogSpecification.containsOriginalUserFullName(originalUserFullName));
        }

        if (updatedByUserFullName != null && !updatedByUserFullName.isBlank()) {
            spec = spec.and(StudentAssignmentLogSpecification.containsUpdatedByUserFullName(updatedByUserFullName));
        }

        return studentAssignmentLogRepository.findAll(spec, sort).stream()
                .map(studentAssignmentLogMapper::toDTO)
                .toList();
    }

    public StudentAssignmentLogResponseDTO deleteStudentAssignmentLogById(Integer id) {
        StudentAssignmentLogEntity studentAssignmentLog = studentAssignmentLogRepository.findById(id)
                .orElseThrow(() -> new StudentAssignmentLogNotFoundException(""));
        studentAssignmentLogRepository.deleteById(id);
        return studentAssignmentLogMapper.toDTO(studentAssignmentLog);
    }

    @Transactional
    public void softDeleteStudentAssignmentLogByUserId(Integer userId) {
        studentAssignmentLogRepository.softDeleteByOriginalUserId(userId);
    }

    @Transactional
    public void softDeleteStudentAssignmentLogsByStudentAssignmentId(Integer studentAssignmentId) {
        studentAssignmentLogRepository.softDeleteByStudentAssignmentId(studentAssignmentId);
    }

    @Transactional
    public void softDeleteStudentAssignmentLogsByStudentAssignmentIds(List<Integer> studentAssignmentIds) {
        if (studentAssignmentIds.isEmpty()) return;
        studentAssignmentLogRepository.softDeleteByStudentAssignmentIds(studentAssignmentIds);
    }
}
