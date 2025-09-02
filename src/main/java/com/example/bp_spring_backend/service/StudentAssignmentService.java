package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.StudentAssignmentEntity;
import com.example.bp_spring_backend.domains.entity.UserEntity;
import com.example.bp_spring_backend.domains.inputDTO.StudentAssignmentRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.StudentAssignmentResponseDTO;
import com.example.bp_spring_backend.exception.StudentAssignmentNotFoundException;
import com.example.bp_spring_backend.mapper.StudentAssignmentMapper;
import com.example.bp_spring_backend.repository.StudentAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentAssignmentService {

    private final StudentAssignmentRepository studentAssignmentRepository;
    private final StudentAssignmentMapper studentAssignmentMapper;
    private final AssignmentService assignmentService;
    private final StudentService studentService;
    private final ExerciseSessionService exerciseSessionService;

    public List<StudentAssignmentResponseDTO> getAllStudentAssignments() {
        List<StudentAssignmentEntity> studentAssignments = studentAssignmentRepository.findAll();
        return studentAssignments.stream()
                .map(studentAssignmentMapper::toDTO)
                .toList();
    }

    public StudentAssignmentResponseDTO getStudentAssignmentById(Integer id) {
        StudentAssignmentEntity studentAssignment = studentAssignmentRepository.findById(id)
                .orElseThrow(() -> new StudentAssignmentNotFoundException(""));
        return studentAssignmentMapper.toDTO(studentAssignment);
    }

    public StudentAssignmentResponseDTO addStudentAssignment(StudentAssignmentRequestDTO request) {
        StudentAssignmentEntity studentAssignment = studentAssignmentMapper.toEntity(
                request,
                assignmentService.getAssignmentEntityById(request.getAssignmentId()),
                studentService.getStudentEntityById(request.getStudentId()),
                exerciseSessionService.getExerciseSessionEntityById(request.getExerciseSessionId()),
                (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
                LocalDateTime.now(),
                null,
                null
        );

        studentAssignmentRepository.save(studentAssignment);

        return studentAssignmentMapper.toDTO(studentAssignment);
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

        studentAssignment.setUpdatedBy((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        studentAssignment.setUpdatedAt(LocalDateTime.now());

        studentAssignment = studentAssignmentRepository.save(studentAssignment);

        return studentAssignmentMapper.toDTO(studentAssignment);
    }
}
