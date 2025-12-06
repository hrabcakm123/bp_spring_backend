package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.*;
import com.example.bp_spring_backend.domains.enums.RoleEnum;
import com.example.bp_spring_backend.domains.inputDTO.UserExerciseRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.ExerciseSummaryResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.UserExerciseResponseDTO;
import com.example.bp_spring_backend.exception.CustomValidationException;
import com.example.bp_spring_backend.exception.UserExerciseNotFoundException;
import com.example.bp_spring_backend.mapper.UserExerciseMapper;
import com.example.bp_spring_backend.repository.UserExerciseRepository;
import com.example.bp_spring_backend.specification.UserExerciseSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserExerciseService {

    private final UserExerciseRepository userExerciseRepository;
    private final UserExerciseMapper userExerciseMapper;
    private final UserService userService;
    private final ExerciseService exerciseService;

    public List<UserExerciseResponseDTO> getUserExercisesByCriteria(Integer id, Sort sort) {
        Specification<UserExerciseEntity> spec = (root, query, builder) -> null;
        if (id != null) {
            spec = spec.and(UserExerciseSpecification.hasId(id));
        }

        return userExerciseRepository.findAll(spec, sort).stream()
                .map(userExerciseMapper::toDTO)
                .toList();
    }

    public List<ExerciseSummaryResponseDTO> getExercisesForCurrentUser() {
        return userExerciseRepository.findExercisesByUserId(((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId())
                .stream()
                .map(userExerciseMapper::toSummaryDTO)
                .toList();
    }

    public List<UserExerciseResponseDTO> addUserExercises(List<UserExerciseRequestDTO> request) {
        if (request == null) {
            throw new CustomValidationException("List name is wrong or missing.");
        }
        List<UserExerciseEntity> userExercises = request.stream()
                .map(dto -> userExerciseMapper.toEntity(
                        userService.getUserEntityById(dto.getUserId()),
                        exerciseService.getExerciseEntityById(dto.getExerciseId())
                ))
                .toList();

        List<UserExerciseEntity> savedUserExercises = userExerciseRepository.saveAll(userExercises);

        return savedUserExercises.stream()
                .map(userExerciseMapper::toDTO)
                .toList();
    }

    public UserExerciseResponseDTO deleteUserExerciseById(Integer id) {
        UserExerciseEntity userExercise = userExerciseRepository.findById(id)
                .orElseThrow(() -> new UserExerciseNotFoundException(""));
        userExerciseRepository.deleteById(id);
        return userExerciseMapper.toDTO(userExercise);
    }

    public UserExerciseResponseDTO updateUserExerciseById(Integer id, UserExerciseRequestDTO request) {
        UserExerciseEntity userExercise = userExerciseRepository.findById(id)
                .orElseThrow(() -> new UserExerciseNotFoundException(""));

        if (request.getUserId() != null) {
            userExercise.setUserEntity(userService.getUserEntityById(request.getUserId()));
        }
        if (request.getExerciseId() != null) {
            userExercise.setExerciseEntity(exerciseService.getExerciseEntityById(request.getExerciseId()));
        }

        userExercise = userExerciseRepository.save(userExercise);

        return userExerciseMapper.toDTO(userExercise);
    }

    public List<UserEntity> getUsersForExercise(Integer exerciseId) {
        return userExerciseRepository.findUsersByExerciseId(exerciseId)
                .stream()
                .filter(user -> user.getRoleEnum() != RoleEnum.SYSTEM)
                .toList();
    }

    @Transactional
    public void softDeleteUserExercisesByUserId(Integer userId) {
        userExerciseRepository.softDeleteByUserId(userId);
    }

    @Transactional
    public void softDeleteUserExercisesByExerciseId(Integer exerciseId) {
        userExerciseRepository.softDeleteByExerciseId(exerciseId);
    }
}
