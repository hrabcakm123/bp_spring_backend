package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.StudentAssignmentEntity;
import com.example.bp_spring_backend.domains.entity.UserEntity;
import com.example.bp_spring_backend.domains.inputDTO.StudentAssignmentRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.StudentAssignmentResponseDTO;
import com.example.bp_spring_backend.exception.CustomValidationException;
import com.example.bp_spring_backend.exception.StudentAssignmentNotFoundException;
import com.example.bp_spring_backend.mapper.StudentAssignmentMapper;
import com.example.bp_spring_backend.repository.StudentAssignmentRepository;
import com.example.bp_spring_backend.specification.StudentAssignmentSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudentAssignmentService {

    private final StudentAssignmentRepository studentAssignmentRepository;
    private final StudentAssignmentMapper studentAssignmentMapper;
    private final AssignmentService assignmentService;
    private final StudentService studentService;
    private final ExerciseSessionService exerciseSessionService;
    private final UserService userService;

    public List<StudentAssignmentResponseDTO> getStudentAssignmentsByCriteria(Map<String, Object> searchCriteria, Sort sort) {
        Specification<StudentAssignmentEntity> spec = (root, query, builder) -> null;
        if (searchCriteria.containsKey("id")) {
            spec = spec.and(StudentAssignmentSpecification.hasId((Integer) searchCriteria.get("id")));
        }

        return studentAssignmentRepository.findAll(spec, sort).stream()
                .map(studentAssignmentMapper::toDTO)
                .toList();
    }

    public List<StudentAssignmentResponseDTO> addStudentAssignments(List<StudentAssignmentRequestDTO> request) {
        if (request == null) {
            throw new CustomValidationException("List name is wrong or missing.");
        }
        List<StudentAssignmentEntity> studentAssignments = request.stream()
                .map(dto -> studentAssignmentMapper.toEntity(
                        dto,
                        assignmentService.getAssignmentEntityById(dto.getAssignmentId()),
                        studentService.getStudentEntityById(dto.getStudentId()),
                        exerciseSessionService.getExerciseSessionEntityById(dto.getExerciseSessionId()),
                        userService.getUserEntityById(((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()),
                        LocalDateTime.now(),
                        null,
                        null
                ))
                .toList();

        List<StudentAssignmentEntity> savedStudentAssignments = studentAssignmentRepository.saveAll(studentAssignments);

        return savedStudentAssignments.stream()
                .map(studentAssignmentMapper::toDTO)
                .toList();
    }

    public StudentAssignmentResponseDTO deleteStudentAssignmentById(Integer id) {
        StudentAssignmentEntity studentAssignment = studentAssignmentRepository.findById(id)
                .orElseThrow(() -> new StudentAssignmentNotFoundException(""));
        studentAssignmentRepository.deleteById(id);
        return studentAssignmentMapper.toDTO(studentAssignment);
    }

    public StudentAssignmentResponseDTO updateStudentAssignmentById(Integer id, StudentAssignmentRequestDTO request) {
        StudentAssignmentEntity studentAssignment = studentAssignmentRepository.findById(id)
                .orElseThrow(() -> new StudentAssignmentNotFoundException(""));

        if (request.getAssignmentId() != null) {
            studentAssignment.setAssignmentEntity(assignmentService.getAssignmentEntityById(request.getAssignmentId()));
        }
        if (request.getStudentId() != null) {
            studentAssignment.setStudentEntity(studentService.getStudentEntityById(request.getStudentId()));
        }
        if (request.getExerciseSessionId() != null) {
            studentAssignment.setExerciseSessionEntity(exerciseSessionService.getExerciseSessionEntityById(request.getExerciseSessionId()));
        }
        if (request.getEarnedPoints() != null) {
            studentAssignment.setEarnedPoints(request.getEarnedPoints());
        }

        studentAssignment.setUpdatedBy(userService.getUserEntityById(((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()));
        studentAssignment.setUpdatedAt(LocalDateTime.now());

        studentAssignment = studentAssignmentRepository.save(studentAssignment);

        return studentAssignmentMapper.toDTO(studentAssignment);
    }
}
