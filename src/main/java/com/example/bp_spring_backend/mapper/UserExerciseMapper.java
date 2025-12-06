package com.example.bp_spring_backend.mapper;

import com.example.bp_spring_backend.domains.entity.*;
import com.example.bp_spring_backend.domains.outputDTO.ExerciseSummaryResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.UserExerciseResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class UserExerciseMapper {

    private final UserMapper userMapper;
    private final ExerciseMapper exerciseMapper;

    public UserExerciseResponseDTO toDTO(UserExerciseEntity userExerciseEntity) {
        return UserExerciseResponseDTO.builder()
                .id(userExerciseEntity.getId())
                .user(userMapper.toDTO(userExerciseEntity.getUserEntity()))
                .exercise(exerciseMapper.toDTO(userExerciseEntity.getExerciseEntity()))
                .build();
    }

    public ExerciseSummaryResponseDTO toSummaryDTO(ExerciseEntity exerciseEntity) {
        return ExerciseSummaryResponseDTO.builder()
                .exerciseId(exerciseEntity.getId())
                .sessionDay(exerciseEntity.getFirstSessionDate().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("sk", "SK")))
                .startTime(exerciseEntity.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                .build();
    }

    public UserExerciseEntity toEntity(UserEntity userEntity, ExerciseEntity exerciseEntity) {
        return UserExerciseEntity.builder()
                .userEntity(userEntity)
                .exerciseEntity(exerciseEntity)
                .build();
    }
}
