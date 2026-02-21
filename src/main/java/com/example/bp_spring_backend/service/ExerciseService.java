package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.ExerciseEntity;
import com.example.bp_spring_backend.domains.inputDTO.ExerciseRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.ExerciseResponseDTO;
import com.example.bp_spring_backend.exception.CustomValidationException;
import com.example.bp_spring_backend.exception.ExerciseNotFoundException;
import com.example.bp_spring_backend.mapper.ExerciseMapper;
import com.example.bp_spring_backend.repository.ExerciseRepository;
import com.example.bp_spring_backend.specification.ExerciseSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseService {
    
    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;

    public ExerciseEntity getExerciseEntityById(Integer id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new ExerciseNotFoundException(""));
    }

    public List<ExerciseEntity> getExerciseEntitiesByIds(List<Integer> ids) {
        return  exerciseRepository.findAllById(ids);
    }

    public List<ExerciseResponseDTO> getExercisesByCriteria(Integer id, Sort sort) {
        Specification<ExerciseEntity> spec = (root, query, builder) -> null;
        if (id != null) {
            spec = spec.and(ExerciseSpecification.hasId(id));
        }

        return exerciseRepository.findAll(spec, sort).stream()
                .map(exerciseMapper::toDTO)
                .toList();
    }

    public List<ExerciseEntity> addExerciseEntities(List<ExerciseRequestDTO> request) {
        if (request == null) {
            throw new CustomValidationException("List name is wrong or missing.");
        }
        List<ExerciseEntity> exercises = request.stream()
                .map(exerciseMapper::toEntity)
                .toList();

        return exerciseRepository.saveAll(exercises);
    }

    public void deleteExerciseById(Integer id) {
        exerciseRepository.findById(id)
                .orElseThrow(() -> new ExerciseNotFoundException(""));
        exerciseRepository.deleteById(id);
    }

    public ExerciseEntity updateExerciseEntityById(Integer id, ExerciseRequestDTO request) {
        ExerciseEntity exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ExerciseNotFoundException(""));

        if (request.getFirstSessionDate() != null) {
            exercise.setFirstSessionDate(request.getFirstSessionDate());
        }
        if (request.getStartTime() != null) {
            exercise.setStartTime(request.getStartTime());
        }
        if (request.getRoomEnum() != null) {
            exercise.setRoomEnum(request.getRoomEnum());
        }

        return exerciseRepository.save(exercise);
    }
}
