package com.example.bp_spring_backend.feature.student;
import com.example.bp_spring_backend.feature.assignment.AssignmentEntity;
import com.example.bp_spring_backend.feature.assignment.AssignmentService;
import com.example.bp_spring_backend.feature.studentAssignment.StudentAssignmentService;
import com.example.bp_spring_backend.feature.studentAssignmentLog.StudentAssignmentLogService;
import com.example.bp_spring_backend.feature.studentAttendance.StudentAttendanceService;
import com.example.bp_spring_backend.feature.studentExercise.StudentExerciseService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final StudentAssignmentLogService studentAssignmentLogService;
    private static final Logger log = LoggerFactory.getLogger(StudentManagerService.class);

    @Transactional
    public void addStudentsWithRelations(StudentToExerciseRequestDTO request) {
        log.info("Adding {} students to exercise id {} with relations", request.getStudents().size(), request.getExerciseId());
        List<StudentEntity> students = studentService.addStudentEntities(request.getStudents());
        log.info("Added {} students to DB", students.size());
        studentExerciseService.addStudentsToExercise(students, request.getExerciseId());
        log.info("Added {} students to exercise id {}", students.size(), request.getExerciseId());
        studentAttendanceService.addInitialAttendancesForStudents(students, request.getExerciseId());
        log.info("Created initial attendances for {} students for exercise id {}", students.size(), request.getExerciseId());
        List<AssignmentEntity> allAssignments = assignmentService.getAllAssignments();
        studentAssignmentService.createAssignmentsForStudents(students, allAssignments);
        log.info("Created assignments for {} students ({} total assignments)", students.size(), allAssignments.size());
    }

    @Transactional
    public void softDeleteStudentCascade(Integer studentId) {
        log.info("Starting soft Delete Student Cascade for student id {}", studentId);
        studentService.deleteStudentById(studentId);
        log.info("Soft deleted student entity");
        studentExerciseService.softDeleteStudentExercisesByStudentId(studentId);
        log.info("Soft deleted student exercises for student id {}", studentId);
        studentAttendanceService.softDeleteStudentAttendancesByStudentId(studentId);
        log.info("Soft deleted student attendances for student id {}", studentId);

        List<Integer> studentAssignmentIds = studentAssignmentService.softDeleteStudentAssignmentsByStudentId(studentId);
        log.info("Soft deleted {} student assignments for student id {}", studentAssignmentIds.size(), studentId);
        if (!studentAssignmentIds.isEmpty()) {
            studentAssignmentLogService.softDeleteStudentAssignmentLogsByStudentAssignmentIds(studentAssignmentIds);
            log.info("Soft deleted student assignment logs for {} student assignments", studentAssignmentIds.size());
        } else {
            log.info("No student assignment logs to soft delete for student id {}", studentId);
        }
        log.info("Completed soft Delete Student Cascade for student id {}", studentId);
    }
}
