package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.AssignmentEntity;
import com.example.bp_spring_backend.domains.entity.StudentEntity;
import com.example.bp_spring_backend.domains.inputDTO.AssignmentRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.AssignmentResponseDTO;
import com.example.bp_spring_backend.mapper.AssignmentMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentManagerService {

    private final AssignmentService assignmentService;
    private final StudentAssignmentService studentAssignmentService;
    private final StudentService studentService;
    private final StudentAssignmentLogService studentAssignmentLogService;
    private final AssignmentMapper assignmentMapper;
    private static final Logger log = LoggerFactory.getLogger(AssignmentManagerService.class);

    @Transactional
    public List<AssignmentResponseDTO> addAssignmentsWithStudentAssignments(List<AssignmentRequestDTO> request) {
        log.info("Adding {} assignments with student assignments", request.size());
        List<AssignmentEntity> assignments = assignmentService.addAssignmentEntities(request);
        log.info("Added {} assignments to DB", assignments.size());
        List<StudentEntity> students = studentService.getAllStudents();
        log.info("Found {} students to assign", students.size());
        studentAssignmentService.createAssignmentsForStudents(students, assignments);
        log.info("Created student assignments for all students");
        return assignments.stream().map(assignmentMapper::toDTO).toList();
    }

    @Transactional
    public AssignmentResponseDTO softDeleteAssignmentCascade(Integer assignmentId) {
        log.info("Starting soft delete cascade for assignment id {}", assignmentId);
        AssignmentResponseDTO response = assignmentService.deleteAssignmentById(assignmentId);
        log.debug("Soft deleted assignment entity: {}", response);
        List<Integer> studentAssignmentIds = studentAssignmentService.softDeleteStudentAssignmentsByAssignmentId(assignmentId);
        log.info("Soft deleted {} student assignments for assignment id {}", studentAssignmentIds.size(), assignmentId);
        if (!studentAssignmentIds.isEmpty()) {
            studentAssignmentLogService.softDeleteStudentAssignmentLogsByStudentAssignmentIds(studentAssignmentIds);
            log.info("Soft deleted student assignment logs for {} assignments", studentAssignmentIds.size());
        } else {
            log.info("No student assignment logs to soft delete for assignment id {}", assignmentId);
        }
        log.info("Completed soft delete cascade for assignment id {}", assignmentId);
        return response;
    }
}
