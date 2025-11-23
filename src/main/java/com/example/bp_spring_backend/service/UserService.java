package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.UserEntity;
import com.example.bp_spring_backend.domains.enums.RoleEnum;
import com.example.bp_spring_backend.domains.inputDTO.UserRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.UserResponseDTO;
import com.example.bp_spring_backend.email.EmailSenderService;
import com.example.bp_spring_backend.email.EmailTemplateBuilder;
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

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService emailSenderService;
    private final EmailTemplateBuilder emailTemplateBuilder;

    private boolean isSystemUser(UserEntity user) {
        return user.getRoleEnum().equals(RoleEnum.SYSTEM);
    }

    public UserEntity getSystemUser(String fullName) {
        return userRepository.findByFullNameAndRoleEnum(fullName, RoleEnum.SYSTEM)
                .orElseThrow(() -> new UserNotFoundException(""));
    }

    public UserEntity getUserEntityById(Integer id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(""));
        if (isSystemUser(user)) throw new UserNotFoundException("");
        return user;
    }

    public List<UserResponseDTO> getUsersByCriteria(Integer id, String email, Sort sort) {
        Specification<UserEntity> spec = (root, query, builder) -> null;
        if (id != null) {
            spec = spec.and(UserSpecification.hasId(id));
        }
        if (email != null) {
            spec = spec.and(UserSpecification.containsEmail(email));
        }

        List<UserEntity> users = userRepository.findAll(spec, sort);
        users.removeIf(this::isSystemUser);

        return users.stream()
                .map(userMapper::toDTO)
                .toList();
    }

    public List<UserResponseDTO> addUsers(List<UserRequestDTO> request) {
        if (request == null) {
            throw new CustomValidationException("List name is wrong or missing.");
        }

        List<UserEntity> users = new ArrayList<>();
        Map<String, String> emailToRawPasswordMap = new HashMap<>();

        for (UserRequestDTO dto : request) {
            String rawPassword = generatePassword();
            String encodedPassword = passwordEncoder.encode(rawPassword);

            emailToRawPasswordMap.put(dto.getEmail(), rawPassword);

            UserEntity user = userMapper.toEntity(dto, encodedPassword);

            if (isSystemUser(user)) throw new CustomValidationException("Cannot add SYSTEM user");

            users.add(user);
        }

        List<UserEntity> savedUsers = userRepository.saveAll(users);

        /*
        for (UserEntity user : savedUsers) {
            String rawPassword = emailToRawPasswordMap.get(user.getEmail());
            emailSenderService.sendEmail(
                    user.getEmail(),
                    "[AP] Oznámenie o vytvorení účtu",
                    emailTemplateBuilder.buildWelcomeText(
                            user.getFullName(),
                            user.getEmail(),
                            rawPassword
                    )
            );
        }
        */
        System.out.println("Email sent ...");

        return savedUsers.stream()
                .map(userMapper::toDTO)
                .toList();
    }

    public UserResponseDTO deleteUserById(Integer id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(""));
        if (isSystemUser(user)) throw new CustomValidationException("Cannot delete SYSTEM user");
        userRepository.deleteById(id);
        return userMapper.toDTO(user);
    }

    public UserResponseDTO updateUserById(Integer id, UserRequestDTO request) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(""));

        if (isSystemUser(user)) throw new CustomValidationException("Cannot update SYSTEM user");

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getEmail() != null) {
            if (!user.getEmail().equals(request.getEmail())) {
                user.setEmail(request.getEmail());
            }
        }
        if (request.getRoleEnum() != null) {
            user.setRoleEnum(request.getRoleEnum());
        }

        user = userRepository.save(user);

        if ((request.getEmail() != null) && !user.getEmail().equals(request.getEmail())) {
            /*
                emailSenderService.sendEmail(
                        user.getEmail(),
                        "[AP] Oznámenie o zmene prihlasovacích údajov",
                        emailTemplateBuilder.buildUpdatedLoginInfo(
                                user.getFullName(),
                                user.getEmail(),
                               "Vaše heslo zostalo nezmenené"
                        )
                );
                 */
            System.out.println("Email sent ...");
        }

        return userMapper.toDTO(user);
    }

    public UserResponseDTO updateUsersPasswordById(Integer id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(""));

        if (isSystemUser(user)) throw new CustomValidationException("Cannot update SYSTEM user");

        String rawPassword = generatePassword();

        user.setPassword(passwordEncoder.encode(rawPassword));

        userRepository.save(user);

        /*
        emailSenderService.sendEmail(
                user.getEmail(),
                "[AP] Oznámenie o zmene prihlasovacích údajov",
                emailTemplateBuilder.buildUpdatedLoginInfo(
                        user.getFullName(),
                        user.getEmail(),
                        rawPassword
                )
        );
         */
        System.out.println("Email sent ...");

        return userMapper.toDTO(user);
    }

    private String generatePassword() {
        SecureRandom random = new SecureRandom();
        return String.format("%06d", random.nextInt(1000000));
    }
}