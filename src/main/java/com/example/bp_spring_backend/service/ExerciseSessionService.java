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
import java.util.List;
import java.util.Map;

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

    public List<ExerciseSessionResponseDTO> getExerciseSessionsByCriteria(Map<String, Object> searchCriteria, Sort sort) {
        Specification<ExerciseSessionEntity> spec = (root, query, builder) -> null;
        if (searchCriteria.containsKey("id")) {
            spec = spec.and(ExerciseSessionSpecification.hasId((Integer) searchCriteria.get("id")));
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
}
