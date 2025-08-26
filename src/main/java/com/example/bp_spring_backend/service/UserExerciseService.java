package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.*;
import com.example.bp_spring_backend.domains.inputDTO.UserExerciseRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.UserExerciseResponseDTO;
import com.example.bp_spring_backend.exception.UserExerciseNotFoundException;
import com.example.bp_spring_backend.mapper.UserExerciseMapper;
import com.example.bp_spring_backend.repository.UserExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserExerciseService {

    private final UserExerciseRepository userExerciseRepository;
    private final UserExerciseMapper userExerciseMapper;
    private final UserService userService;
    private final ExerciseService exerciseService;

    public List<UserExerciseResponseDTO> getAllUserExercises() {
        List<UserExerciseEntity> userExercises = userExerciseRepository.findAll();
        return userExercises.stream()
                .map(userExerciseMapper::toDTO)
                .toList();
    }

    public UserExerciseResponseDTO getUserExerciseById(Integer id) {
        UserExerciseEntity userExercise = userExerciseRepository.findById(id).orElseThrow(() -> new UserExerciseNotFoundException(""));
        return userExerciseMapper.toDTO(userExercise);
    }

    public List<UserExerciseResponseDTO> addUserExercises(List<UserExerciseRequestDTO> request) {
        List<UserExerciseEntity> userExercises = request.stream()
                .map(dto -> {
                    UserEntity userEntity = userService.getUserEntityById(dto.getUserId());
                    ExerciseEntity exerciseEntity = exerciseService.getExerciseEntityById(dto.getExerciseId());
                    return userExerciseMapper.toEntity(userEntity, exerciseEntity);
                })
                .toList();

        List<UserExerciseEntity> savedUserExercises = userExerciseRepository.saveAll(userExercises);

        return savedUserExercises.stream()
                .map(userExerciseMapper::toDTO)
                .toList();
    }

    public UserExerciseResponseDTO deleteUserExerciseById(Integer id) {
        UserExerciseEntity userExercise = userExerciseRepository.findById(id).orElseThrow(() -> new UserExerciseNotFoundException(""));
        userExerciseRepository.deleteById(id);
        return userExerciseMapper.toDTO(userExercise);
    }

    public UserExerciseResponseDTO updateUserExerciseById(Integer id, UserExerciseRequestDTO request) {
        UserExerciseEntity userExercise = userExerciseRepository.findById(id).orElseThrow(() -> new UserExerciseNotFoundException(""));

        if (request.getUserId() != null) {
            userExercise.setUserEntity(userService.getUserEntityById(request.getUserId()));
        }
        if (request.getExerciseId() != null) {
            userExercise.setExerciseEntity(exerciseService.getExerciseEntityById(request.getExerciseId()));
        }

        userExercise = userExerciseRepository.save(userExercise);

        return userExerciseMapper.toDTO(userExercise);
    }
}
