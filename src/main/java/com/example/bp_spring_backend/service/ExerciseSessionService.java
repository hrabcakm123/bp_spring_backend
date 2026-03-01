package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.*;
import com.example.bp_spring_backend.domains.inputDTO.ExerciseSessionRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.ExerciseSessionResponseDTO;
import com.example.bp_spring_backend.exception.CustomValidationException;
import com.example.bp_spring_backend.exception.ExerciseNotFoundException;
import com.example.bp_spring_backend.exception.ExerciseSessionNotFoundException;
import com.example.bp_spring_backend.mapper.ExerciseSessionMapper;
import com.example.bp_spring_backend.repository.ExerciseSessionRepository;
import com.example.bp_spring_backend.specification.ExerciseSessionSpecification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseSessionService {

    private final ExerciseSessionRepository exerciseSessionRepository;
    private final ExerciseSessionMapper exerciseSessionMapper;
    private final ExerciseService exerciseService;
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(ExerciseSessionService.class);

    public ExerciseSessionEntity getExerciseSessionEntityById(Integer id) {
        return exerciseSessionRepository.findById(id)
                .orElseThrow(() -> new ExerciseSessionNotFoundException(""));
    }

    public List<ExerciseSessionEntity> getExerciseSessionEntitiesByIds(List<Integer> ids) {
        return exerciseSessionRepository.findAllById(ids);
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

    public void addExerciseSessions(List<ExerciseSessionRequestDTO> request, Integer currentUserId) {
        if (request == null) {
            throw new CustomValidationException("List name is wrong or missing.");
        }
        log.info("Adding {} exercise sessions by user id {}", request.size(), currentUserId);
        UserEntity currentUser = userService.getUserEntityById(currentUserId);
        List<Integer> exerciseIds = request.stream()
                .map(ExerciseSessionRequestDTO::getExerciseId)
                .distinct()
                .toList();

        List<ExerciseEntity> exercises = exerciseService.getExerciseEntitiesByIds(exerciseIds);
        Map<Integer, ExerciseEntity> exerciseMap = exercises.stream()
                .collect(Collectors.toMap(ExerciseEntity::getId, Function.identity()));

        List<ExerciseSessionEntity> exerciseSessions = request.stream()
                .map(dto -> {
                    ExerciseEntity exercise = exerciseMap.get(dto.getExerciseId());
                    if (exercise == null) {
                        throw new ExerciseNotFoundException("");
                    }

                    return exerciseSessionMapper.toEntity(
                            dto,
                            exercise,
                            currentUser,
                            LocalDateTime.now(),
                            null,
                            null
                    );
                })
                .toList();

        exerciseSessionRepository.saveAll(exerciseSessions);
        log.info("Saved exercise sessions to DB");
    }

    public void deleteExerciseSessionById(Integer id) {
        exerciseSessionRepository.findById(id)
                .orElseThrow(() -> new ExerciseSessionNotFoundException(""));
        exerciseSessionRepository.deleteById(id);
    }

    public void updateExerciseSessionById(Integer id, ExerciseSessionRequestDTO request, Integer currentUserId) {
        log.info("Updating exercise session id {} by user id {}", id, currentUserId);
        ExerciseSessionEntity exerciseSession = exerciseSessionRepository.findById(id)
                .orElseThrow(() -> new ExerciseSessionNotFoundException(""));

        if (request.getExerciseId() != null) {
            exerciseSession.setExerciseEntity(exerciseService.getExerciseEntityById(request.getExerciseId()));
        }
        if (request.getSessionDate() != null) {
            exerciseSession.setSessionDate(request.getSessionDate());
        }

        exerciseSession.setUpdatedBy(userService.getUserEntityById(currentUserId));
        exerciseSession.setUpdatedAt(LocalDateTime.now());

        exerciseSessionRepository.save(exerciseSession);
        log.info("Saved exercise session entity");
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

    public List<Integer> softDeleteExerciseSessionsByUserId(Integer userId) {

        List<Integer> exerciseSessionIds = exerciseSessionRepository.findExerciseSessionIdsByCreatedOrUpdatedByUserId(userId);

        if (!exerciseSessionIds.isEmpty()) {
            exerciseSessionRepository.softDeleteByCreatedOrUpdatedByUserId(userId);
        }

        return exerciseSessionIds;
    }

    public List<Integer> softDeleteExerciseSessionsByExerciseId(Integer exerciseId) {

        List<Integer> exerciseSessionIds = exerciseSessionRepository.findExerciseSessionIdsByExerciseId(exerciseId);

        if (!exerciseSessionIds.isEmpty()) {
            exerciseSessionRepository.softDeleteByExerciseId(exerciseId);
        }

        return exerciseSessionIds;
    }
}
