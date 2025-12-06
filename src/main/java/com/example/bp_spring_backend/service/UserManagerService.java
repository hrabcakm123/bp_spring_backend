package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.outputDTO.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public UserResponseDTO softDeleteUserCascade(Integer userId) {

        UserResponseDTO response = userService.deleteUserById(userId);

        userExerciseService.softDeleteUserExercisesByUserId(userId);
        exerciseSessionService.softDeleteExerciseSessionsByUserId(userId);
        studentAttendanceService.softDeleteStudentAttendancesByUserId(userId);
        studentAssignmentService.softDeleteStudentAssignmentsByUserId(userId);
        studentAssignmentLogService.softDeleteStudentAssignmentLogByUserId(userId);

        List<Integer> exerciseSessionIds = exerciseSessionService.softDeleteExerciseSessionsByUserId(userId);

        if (!exerciseSessionIds.isEmpty()) {
            studentAttendanceService.softDeleteStudentAttendancesByExerciseSessionIds(exerciseSessionIds);
        }

        List<Integer> studentAssignmentIds = studentAssignmentService.softDeleteStudentAssignmentsByUserId(userId);

        if (!studentAssignmentIds.isEmpty()) {
            studentAssignmentLogService.softDeleteStudentAssignmentLogsByStudentAssignmentIds(studentAssignmentIds);
        }

        return response;
    }
}
