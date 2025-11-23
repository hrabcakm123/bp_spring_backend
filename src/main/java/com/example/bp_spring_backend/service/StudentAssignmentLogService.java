package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.StudentAssignmentEntity;
import com.example.bp_spring_backend.domains.entity.StudentAssignmentLogEntity;
import com.example.bp_spring_backend.domains.entity.UserEntity;
import com.example.bp_spring_backend.domains.outputDTO.StudentAssignmentLogResponseDTO;
import com.example.bp_spring_backend.mapper.StudentAssignmentLogMapper;
import com.example.bp_spring_backend.repository.StudentAssignmentLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

    public List<StudentAssignmentLogResponseDTO> getStudentAssignmentLogs(Sort sort) {
        return studentAssignmentLogRepository.findAll(sort).stream()
                .map(studentAssignmentLogMapper::toDTO)
                .toList();
    }
}
