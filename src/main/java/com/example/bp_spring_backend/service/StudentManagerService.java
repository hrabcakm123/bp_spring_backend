package com.example.bp_spring_backend.service;
import com.example.bp_spring_backend.domains.entity.StudentEntity;
import com.example.bp_spring_backend.domains.inputDTO.StudentToExerciseRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.StudentResponseDTO;
import com.example.bp_spring_backend.mapper.StudentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentManagerService {

    private final StudentService studentService;
    private final StudentExerciseService studentExerciseService;
    private final StudentAttendanceService studentAttendanceService;
    private final StudentAssignmentService studentAssignmentService;
    private final AssignmentService assignmentService;
    private final StudentMapper studentMapper;
    private final StudentAssignmentLogService studentAssignmentLogService;

    @Transactional
    public List<StudentResponseDTO> addStudentsWithRelations(StudentToExerciseRequestDTO request) {

        List<StudentEntity> students = studentService.addStudentEntities(request.getStudents());

        studentExerciseService.addStudentsToExercise(students, request.getExerciseId());
        studentAttendanceService.addInitialAttendancesForStudents(students, request.getExerciseId());
        studentAssignmentService.createAssignmentsForStudents(students, assignmentService.getAllAssignments());

        return students.stream().map(studentMapper::toDTO).toList();
    }

    @Transactional
    public StudentResponseDTO softDeleteStudentCascade(Integer studentId) {

        StudentResponseDTO response = studentService.deleteStudentById(studentId);

        studentExerciseService.softDeleteStudentExercisesByStudentId(studentId);
        studentAttendanceService.softDeleteStudentAttendancesByStudentId(studentId);

        List<Integer> studentAssignmentIds = studentAssignmentService.softDeleteStudentAssignmentsByStudentId(studentId);

        if (!studentAssignmentIds.isEmpty()) {
            studentAssignmentLogService.softDeleteStudentAssignmentLogsByStudentAssignmentIds(studentAssignmentIds);
        }

        return response;
    }
}
