package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.AssignmentEntity;
import com.example.bp_spring_backend.domains.entity.StudentAssignmentEntity;
import com.example.bp_spring_backend.domains.entity.StudentEntity;
import com.example.bp_spring_backend.domains.entity.UserEntity;
import com.example.bp_spring_backend.domains.enums.RoleEnum;
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
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentAssignmentService {

    private final StudentAssignmentRepository studentAssignmentRepository;
    private final StudentAssignmentMapper studentAssignmentMapper;
    private final AssignmentService assignmentService;
    private final StudentService studentService;
    private final UserService userService;
    private final StudentAssignmentLogService studentAssignmentLogService;

    public List<StudentAssignmentResponseDTO> getStudentAssignmentsByCriteria(Integer id, Sort sort) {
        Specification<StudentAssignmentEntity> spec = (root, query, builder) -> null;
        if (id != null) {
            spec = spec.and(StudentAssignmentSpecification.hasId(id));
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

        Double oldPoints = studentAssignment.getEarnedPoints();

        if (request.getAssignmentId() != null) {
            studentAssignment.setAssignmentEntity(assignmentService.getAssignmentEntityById(request.getAssignmentId()));
        }
        if (request.getStudentId() != null) {
            studentAssignment.setStudentEntity(studentService.getStudentEntityById(request.getStudentId()));
        }
        if (request.getEarnedPoints() != null && !request.getEarnedPoints().equals(oldPoints)) {
            studentAssignment.setEarnedPoints(request.getEarnedPoints());
        }
        if (request.getNote() != null) {
            studentAssignment.setNote(request.getNote());
        }

        UserEntity currentUser = userService.getUserEntityById(
                ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()
        );

        studentAssignment.setUpdatedBy(currentUser);
        studentAssignment.setUpdatedAt(LocalDateTime.now());

        studentAssignment = studentAssignmentRepository.save(studentAssignment);

        if (request.getEarnedPoints() != null
                && !request.getEarnedPoints().equals(oldPoints)
                && currentUser.getRoleEnum() == RoleEnum.HELPER
                && studentAssignment.getUpdatedBy() != null
                && (studentAssignment.getUpdatedBy().getRoleEnum() == RoleEnum.ADMIN
                || studentAssignment.getUpdatedBy().getRoleEnum() == RoleEnum.TEACHER)) {

            studentAssignmentLogService.createLog(studentAssignment, oldPoints, request.getEarnedPoints(), currentUser);
        }

        return studentAssignmentMapper.toDTO(studentAssignment);
    }

    public void createAssignmentsForStudents(
            List<StudentEntity> students,
            List<AssignmentEntity> assignments
    ) {

        UserEntity systemUser = userService.getSystemUser("SYSTEM");

        List<StudentAssignmentEntity> toSave =  new ArrayList<>();

        for (StudentEntity student : students) {
            for (AssignmentEntity assignment : assignments) {
                toSave.add(StudentAssignmentEntity.builder()
                        .studentEntity(student)
                        .assignmentEntity(assignment)
                        .earnedPoints(0.0)
                        .note(null)
                        .createdBy(systemUser)
                        .createdAt(LocalDateTime.now())
                        .build());
            }
        }

        studentAssignmentRepository.saveAll(toSave);
    }
}
