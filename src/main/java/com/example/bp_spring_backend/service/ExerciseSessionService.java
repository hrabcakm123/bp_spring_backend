package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.*;
import com.example.bp_spring_backend.domains.inputDTO.ExerciseSessionRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.ExerciseSessionResponseDTO;
import com.example.bp_spring_backend.exception.ExerciseSessionNotFoundException;
import com.example.bp_spring_backend.mapper.ExerciseSessionMapper;
import com.example.bp_spring_backend.repository.ExerciseSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseSessionService {

    private final ExerciseSessionRepository exerciseSessionRepository;
    private final ExerciseSessionMapper exerciseSessionMapper;
    private final ExerciseService exerciseService;

    public List<ExerciseSessionResponseDTO> getAllExerciseSessions() {
        List<ExerciseSessionEntity> exerciseSessions = exerciseSessionRepository.findAll();
        return exerciseSessions.stream()
                .map(exerciseSessionMapper::toDTO)
                .toList();
    }

    public ExerciseSessionResponseDTO getExerciseSessionById(Integer id) {
        ExerciseSessionEntity exerciseSession = exerciseSessionRepository.findById(id)
                .orElseThrow(() -> new ExerciseSessionNotFoundException(""));
        return exerciseSessionMapper.toDTO(exerciseSession);
    }

    public ExerciseSessionEntity getExerciseSessionEntityById(Integer id) {
        return exerciseSessionRepository.findById(id)
                .orElseThrow(() -> new ExerciseSessionNotFoundException(""));
    }

    public ExerciseSessionResponseDTO addExerciseSession(ExerciseSessionRequestDTO request) {
        ExerciseSessionEntity exerciseSession = exerciseSessionMapper.toEntity(
                request,
                exerciseService.getExerciseEntityById(request.getExerciseId()),
                (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
                LocalDateTime.now(),
                null,
                null
        );

        exerciseSessionRepository.save(exerciseSession);

        return exerciseSessionMapper.toDTO(exerciseSession);
    }

    public ExerciseSessionResponseDTO deleteExerciseSessionById(Integer id) {
        ExerciseSessionEntity exerciseSession = exerciseSessionRepository.findById(id)
                .orElseThrow(() -> new ExerciseSessionNotFoundException(""));
        exerciseSessionRepository.deleteById(id);
        return exerciseSessionMapper.toDTO(exerciseSession);
    }

    public ExerciseSessionResponseDTO updateExerciseSessionById(Integer id, ExerciseSessionRequestDTO request) {
        ExerciseSessionEntity exerciseSession = exerciseSessionRepository.findById(id)
                .orElseThrow(() -> new ExerciseSessionNotFoundException(""));

        if (request.getExerciseId() != null) {
            exerciseSession.setExerciseEntity(exerciseService.getExerciseEntityById(request.getExerciseId()));
        }
        if (request.getSessionDate() != null) {
            exerciseSession.setSessionDate(request.getSessionDate());
        }

        exerciseSession.setUpdatedBy((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        exerciseSession.setUpdatedAt(LocalDateTime.now());

        exerciseSession = exerciseSessionRepository.save(exerciseSession);

        return exerciseSessionMapper.toDTO(exerciseSession);
    }
}
