package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.*;
import com.example.bp_spring_backend.domains.inputDTO.ExerciseSessionRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.ExerciseSessionResponseDTO;
import com.example.bp_spring_backend.exception.CustomValidationException;
import com.example.bp_spring_backend.exception.ExerciseSessionNotFoundException;
import com.example.bp_spring_backend.mapper.ExerciseSessionMapper;
import com.example.bp_spring_backend.repository.ExerciseSessionRepository;
import com.example.bp_spring_backend.specification.ExerciseSessionSpecification;
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
public class ExerciseSessionService {

    private final ExerciseSessionRepository exerciseSessionRepository;
    private final ExerciseSessionMapper exerciseSessionMapper;
    private final ExerciseService exerciseService;
    private final UserService userService;

    public ExerciseSessionEntity getExerciseSessionEntityById(Integer id) {
        return exerciseSessionRepository.findById(id)
                .orElseThrow(() -> new ExerciseSessionNotFoundException(""));
    }

    public List<ExerciseSessionResponseDTO> getExerciseSessionsByCriteria(Integer id, Sort sort) {
        Specification<ExerciseSessionEntity> spec = (root, query, builder) -> null;
        if (id != null) {
            spec = spec.and(ExerciseSessionSpecification.hasId(id));
        }

        return exerciseSessionRepository.findAll(spec, sort).stream()
                .map(exerciseSessionMapper::toDTO)
                .toList();
    }

    public List<ExerciseSessionResponseDTO> addExerciseSessions(List<ExerciseSessionRequestDTO> request) {
        if (request == null) {
            throw new CustomValidationException("List name is wrong or missing.");
        }
        List<ExerciseSessionEntity> exerciseSessions = request.stream()
                .map(dto -> exerciseSessionMapper.toEntity(
                        dto,
                        exerciseService.getExerciseEntityById(dto.getExerciseId()),
                        userService.getUserEntityById(((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()),
                        LocalDateTime.now(),
                        null,
                        null
                ))
                .toList();

        List<ExerciseSessionEntity> savedExerciseSessions =  exerciseSessionRepository.saveAll(exerciseSessions);

        return savedExerciseSessions.stream()
                .map(exerciseSessionMapper::toDTO)
                .toList();
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

        exerciseSession.setUpdatedBy(userService.getUserEntityById(((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()));
        exerciseSession.setUpdatedAt(LocalDateTime.now());

        exerciseSession = exerciseSessionRepository.save(exerciseSession);

        return exerciseSessionMapper.toDTO(exerciseSession);
    }

    public List<ExerciseSessionEntity> getSessionsForExerciseDesc(Integer exerciseId) {
        return exerciseSessionRepository.findByExerciseEntityIdOrderBySessionDateDesc(exerciseId);
    }

    public void addInitialSessionsForExercises(List<ExerciseEntity> exercises) {
        UserEntity systemUser = userService.getSystemUser("SYSTEM");

        List<ExerciseSessionEntity> sessions = new ArrayList<>();
        for (ExerciseEntity exerciseEntity : exercises) {
            for (int i = 0; i < 12; i++) {
                ExerciseSessionEntity session = ExerciseSessionEntity.builder()
                        .exerciseEntity(exerciseEntity)
                        .sessionDate(exerciseEntity.getFirstSessionDate().plusDays(7L * i))
                        .createdBy(systemUser)
                        .createdAt(LocalDateTime.now())
                        .build();
                sessions.add(session);
            }
        }

        exerciseSessionRepository.saveAll(sessions);
    }

    public void updateSessionsForExercise(ExerciseEntity exercise) {
        List<ExerciseSessionEntity> sessions = exerciseSessionRepository
                .findByExerciseEntityIdOrderBySessionDateDesc(exercise.getId());

        UserEntity systemUser = userService.getSystemUser("SYSTEM");

        int counter = sessions.size() - 1;
        for (ExerciseSessionEntity session : sessions) {
            session.setSessionDate(exercise.getFirstSessionDate().plusDays(7L * counter));
            session.setUpdatedBy(systemUser);
            session.setUpdatedAt(LocalDateTime.now());
            counter--;
        }

        exerciseSessionRepository.saveAll(sessions);
    }

}
