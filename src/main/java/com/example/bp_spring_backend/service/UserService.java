package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.UserEntity;
import com.example.bp_spring_backend.domains.inputDTO.UserRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.UserResponseDTO;
import com.example.bp_spring_backend.exception.CustomValidationException;
import com.example.bp_spring_backend.exception.UserNotFoundException;
import com.example.bp_spring_backend.mapper.UserMapper;
import com.example.bp_spring_backend.repository.UserRepository;
import com.example.bp_spring_backend.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserEntity getUserEntityById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(""));
    }

    public List<UserResponseDTO> getUsersByCriteria(Integer id, String email, Sort sort) {
        Specification<UserEntity> spec = (root, query, builder) -> null;
        if (id != null) {
            spec = spec.and(UserSpecification.hasId(id));
        }
        if (email != null) {
            spec = spec.and(UserSpecification.containsEmail(email));
        }

        return userRepository.findAll(spec, sort).stream()
                .map(userMapper::toDTO)
                .toList();
    }

    public List<UserResponseDTO> addUsers(List<UserRequestDTO> request) {
        if (request == null) {
            throw new CustomValidationException("List name is wrong or missing.");
        }
        List<UserEntity> users = request.stream()
                .map(userMapper::toEntity)
                .toList();

        for (UserEntity user : users) {
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        }

        List<UserEntity> savedUsers = userRepository.saveAll(users);

        return savedUsers.stream()
                .map(userMapper::toDTO)
                .toList();
    }

    public UserResponseDTO deleteUserById(Integer id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(""));
        userRepository.deleteById(id);
        return userMapper.toDTO(user);
    }

    public UserResponseDTO updateUserById(Integer id, UserRequestDTO request) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(""));

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getRoleEnum() != null) {
            user.setRoleEnum(request.getRoleEnum());
        }

        user = userRepository.save(user);

        return userMapper.toDTO(user);
    }
}
