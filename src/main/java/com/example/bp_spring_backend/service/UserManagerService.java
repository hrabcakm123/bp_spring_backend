package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.outputDTO.UserResponseDTO;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public UserResponseDTO softDeleteUserCascade(Integer userId) {

        UserResponseDTO response = userService.deleteUserById(userId);

        userExerciseService.softDeleteUserExercisesByUserId(userId);
        List<Integer> exerciseSessionIds = exerciseSessionService.softDeleteExerciseSessionsByUserId(userId);
        if (!exerciseSessionIds.isEmpty()) {
            studentAttendanceService.softDeleteStudentAttendancesByExerciseSessionIds(exerciseSessionIds);
        }
        studentAttendanceService.softDeleteStudentAttendancesByUserId(userId);
        List<Integer> studentAssignmentIds = studentAssignmentService.softDeleteStudentAssignmentsByUserId(userId);
        if (!studentAssignmentIds.isEmpty()) {
            studentAssignmentLogService.softDeleteStudentAssignmentLogsByStudentAssignmentIds(studentAssignmentIds);
        }
        studentAssignmentLogService.softDeleteStudentAssignmentLogsByUserId(userId);

        return response;
    }
}
