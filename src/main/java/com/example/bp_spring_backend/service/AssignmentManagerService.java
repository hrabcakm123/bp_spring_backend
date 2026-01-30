package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.AssignmentEntity;
import com.example.bp_spring_backend.domains.entity.StudentEntity;
import com.example.bp_spring_backend.domains.inputDTO.AssignmentRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.AssignmentResponseDTO;
import com.example.bp_spring_backend.mapper.AssignmentMapper;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public List<AssignmentResponseDTO> addAssignmentsWithStudentAssignments(List<AssignmentRequestDTO> request) {

        List<AssignmentEntity> assignments = assignmentService.addAssignmentEntities(request);
        List<StudentEntity> students = studentService.getAllStudents();

        studentAssignmentService.createAssignmentsForStudents(students, assignments);

        return assignments.stream().map(assignmentMapper::toDTO).toList();
    }

    @Transactional
    public AssignmentResponseDTO softDeleteAssignmentCascade(Integer assignmentId) {

        AssignmentResponseDTO response = assignmentService.deleteAssignmentById(assignmentId);

        List<Integer> studentAssignmentIds = studentAssignmentService.softDeleteStudentAssignmentsByAssignmentId(assignmentId);

        if (!studentAssignmentIds.isEmpty()) {
            studentAssignmentLogService.softDeleteStudentAssignmentLogsByStudentAssignmentIds(studentAssignmentIds);
        }

        return response;
    }
}
