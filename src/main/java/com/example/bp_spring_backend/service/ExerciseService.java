package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.ExerciseEntity;
import com.example.bp_spring_backend.domains.inputDTO.ExerciseRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.ExerciseResponseDTO;
import com.example.bp_spring_backend.exception.CustomValidationException;
import com.example.bp_spring_backend.exception.ExerciseNotFoundException;
import com.example.bp_spring_backend.mapper.ExerciseMapper;
import com.example.bp_spring_backend.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseService {
    
    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;

    public List<ExerciseResponseDTO> getAllExercises() {
        List<ExerciseEntity> exercises = exerciseRepository.findAll();
        return exercises.stream()
                .map(exerciseMapper::toDTO)
                .toList();
    }

    public ExerciseResponseDTO getExerciseById(Integer id) {
        ExerciseEntity exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ExerciseNotFoundException(""));
        return exerciseMapper.toDTO(exercise);
    }

    public ExerciseEntity getExerciseEntityById(Integer id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new ExerciseNotFoundException(""));
    }

    public List<ExerciseResponseDTO> addExercises(List<ExerciseRequestDTO> request) {
        if (request == null) {
            throw new CustomValidationException("List name is wrong or missing.");
        }
        List<ExerciseEntity> exercises = request.stream()
                .map(exerciseMapper::toEntity)
                .toList();

        for (ExerciseEntity exercise : exercises) {
            validateTimeOrder(exercise.getStartTime(), exercise.getEndTime());
        }

        List<ExerciseEntity> savedUsers = exerciseRepository.saveAll(exercises);

        return savedUsers.stream()
                .map(exerciseMapper::toDTO)
                .toList();
    }

    public ExerciseResponseDTO deleteExerciseById(Integer id) {
        ExerciseEntity user = exerciseRepository.findById(id)
                .orElseThrow(() -> new ExerciseNotFoundException(""));
        exerciseRepository.deleteById(id);
        return exerciseMapper.toDTO(user);
    }

    public ExerciseResponseDTO updateExerciseById(Integer id, ExerciseRequestDTO request) {
        ExerciseEntity exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ExerciseNotFoundException(""));

        if (request.getDayOfWeek() != null) {
            exercise.setDayOfWeek(request.getDayOfWeek());
        }
        if (request.getStartTime() != null) {
            exercise.setStartTime(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            exercise.setEndTime(request.getEndTime());
        }
        if (request.getRoomEnum() != null) {
            exercise.setRoomEnum(request.getRoomEnum());
        }

        validateTimeOrder(exercise.getStartTime(), exercise.getEndTime());

        exercise = exerciseRepository.save(exercise);

        return exerciseMapper.toDTO(exercise);
    }

    private void validateTimeOrder(LocalTime startTime, LocalTime endTime) {
        if (startTime != null && endTime != null && !endTime.isAfter(startTime)) {
            throw new CustomValidationException("endTime must be after startTime.");
        }
    }
}
