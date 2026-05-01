package com.example.bp_spring_backend.feature.user;

import com.example.bp_spring_backend.feature.exerciseSession.ExerciseSessionService;
import com.example.bp_spring_backend.feature.studentAssignment.StudentAssignmentService;
import com.example.bp_spring_backend.feature.studentAssignmentLog.StudentAssignmentLogService;
import com.example.bp_spring_backend.feature.studentAttendance.StudentAttendanceService;
import com.example.bp_spring_backend.feature.userExercise.UserExerciseService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserManagerService {
    private final UserService userService;
    private final UserExerciseService userExerciseService;
    private final ExerciseSessionService exerciseSessionService;
    private final StudentAttendanceService studentAttendanceService;
    private final StudentAssignmentService studentAssignmentService;
    private final StudentAssignmentLogService studentAssignmentLogService;
    private static final Logger log = LoggerFactory.getLogger(UserManagerService.class);

    @Transactional
    public void softDeleteUserCascade(Integer userId) {
        log.info("Starting soft Delete User Cascade for user id {}", userId);
        userService.deleteUserById(userId);
        log.info("Soft Deleted user entity");
        userExerciseService.softDeleteUserExercisesByUserId(userId);
        log.info("Soft deleted user exercises for user id {}", userId);
        List<Integer> exerciseSessionIds = exerciseSessionService.softDeleteExerciseSessionsByUserId(userId);
        log.info("Soft deleted {} exercise sessions for user id {}", exerciseSessionIds.size(), userId);
        if (!exerciseSessionIds.isEmpty()) {
            studentAttendanceService.softDeleteStudentAttendancesByExerciseSessionIds(exerciseSessionIds);
            log.info("Soft deleted student attendances for {} exercise sessions", exerciseSessionIds.size());
        }
        studentAttendanceService.softDeleteStudentAttendancesByUserId(userId);
        log.info("Soft deleted student attendances directly for user id {}", userId);
        List<Integer> studentAssignmentIds = studentAssignmentService.softDeleteStudentAssignmentsByUserId(userId);
        log.info("Soft deleted {} student assignments for user id {}", studentAssignmentIds.size(), userId);
        if (!studentAssignmentIds.isEmpty()) {
            studentAssignmentLogService.softDeleteStudentAssignmentLogsByStudentAssignmentIds(studentAssignmentIds);
            log.info("Soft deleted student assignment logs for {} student assignments", studentAssignmentIds.size());
        }
        studentAssignmentLogService.softDeleteStudentAssignmentLogsByUserId(userId);
        log.info("Soft deleted student assignment logs directly for user id {}", userId);
        log.info("Completed soft Delete User Cascade for user id {}", userId);
    }
}
