package com.example.bp_spring_backend.feature.user;

import com.example.bp_spring_backend.feature.enums.RoleEnum;
import com.example.bp_spring_backend.core.email.EmailSenderService;
import com.example.bp_spring_backend.core.email.EmailTemplateBuilder;
import com.example.bp_spring_backend.core.exception.CustomValidationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

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

    public List<UserEntity> getUserEntitiesByIds(List<Integer> ids) {
        return userRepository.findAllById(ids);
    }

    public List<UserResponseDTO> getUsersByCriteria(Integer id, String email, String fullName, Sort sort) {
        Specification<UserEntity> spec = (root, query, builder) -> null;
        if (id != null) {
            spec = spec.and(UserSpecification.hasId(id));
        }
        if (email != null) {
            spec = spec.and(UserSpecification.containsEmail(email));
        }
        if (fullName != null) {
            spec = spec.and(UserSpecification.containsFullName(fullName));
        }

        List<UserEntity> users = userRepository.findAll(spec, sort);
        users.removeIf(this::isSystemUser);

        return users.stream()
                .map(userMapper::toDTO)
                .toList();
    }

    public void addUsers(List<UserRequestDTO> request) {
        if (request == null) {
            throw new CustomValidationException("List name is wrong or missing.");
        }
        log.info("Adding {} users", request.size());
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
        log.info("Saved {} users to DB", savedUsers.size());

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
            log.info("Welcome email send to {} with generated password", user.getEmail());
        }

        //System.out.println("Email sent ...");
    }

    public void deleteUserById(Integer id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(""));
        if (isSystemUser(user)) throw new CustomValidationException("Cannot delete SYSTEM user");
        userRepository.deleteById(id);
    }

    public void updateUserById(Integer id, UserRequestDTO request) {
        log.info("Updating user id {}", id);
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(""));

        if (isSystemUser(user)) throw new CustomValidationException("Cannot update SYSTEM user");

        String oldEmail = user.getEmail();
        boolean emailChanged = false;

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getEmail() != null && !request.getEmail().equals(oldEmail)) {
            user.setEmail(request.getEmail());
            emailChanged = true;
        }
        if (request.getRoleEnum() != null) {
            user.setRoleEnum(request.getRoleEnum());
        }

        user = userRepository.save(user);
        log.info("Saved user entity id {}", user.getId());

        if (emailChanged) {

            emailSenderService.sendEmail(
                    oldEmail,
                    "[AP] Oznámenie o zmene prihlasovacích údajov",
                    emailTemplateBuilder.buildUpdatedLoginInfo(
                            user.getFullName(),
                            user.getEmail(),
                            "Vaše heslo zostalo nezmenené"
                    )
            );

            emailSenderService.sendEmail(
                    user.getEmail(),
                    "[AP] Oznámenie o zmene prihlasovacích údajov",
                    emailTemplateBuilder.buildUpdatedLoginInfo(
                            user.getFullName(),
                            user.getEmail(),
                            "Vaše heslo zostalo nezmenené"
                    )
            );
            log.info("Updated login sent to old {} and new {} email of user", oldEmail, user.getEmail());
            //System.out.println("Email sent ...");
        }
    }

    public void updateUsersPasswordById(Integer id) {
        log.info("Updating password for user id {}", id);
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(""));

        if (isSystemUser(user)) throw new CustomValidationException("Cannot update SYSTEM user");

        String rawPassword = generatePassword();

        user.setPassword(passwordEncoder.encode(rawPassword));

        userRepository.save(user);
        log.info("Password updated for user id {}", id);

        emailSenderService.sendEmail(
                user.getEmail(),
                "[AP] Oznámenie o zmene prihlasovacích údajov",
                emailTemplateBuilder.buildUpdatedLoginInfo(
                        user.getFullName(),
                        user.getEmail(),
                        rawPassword
                )
        );
        log.info("Updated password sent to email {} of user", user.getEmail());

//        System.out.println("Email sent ...");
    }

    public void updateCurrentUsersPassword(UserPasswordRequestDTO request, Integer currentUserId) {
        log.info("Updating password for user id {}", currentUserId);
        UserEntity user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException(""));

        if (request.getNewPassword().equals(request.getOldPassword())) {
            throw new CustomValidationException("New password must be different from the old password.");
        }

        if (passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
            log.info("Password updated for user id {}", currentUserId);
            emailSenderService.sendEmail(
                    user.getEmail(),
                    "[AP] Oznámenie o zmene hesla",
                    emailTemplateBuilder.buildPasswordChangedInfo(
                            user.getFullName(),
                            user.getEmail()
                    )
            );
            log.info("Updated password sent to email {} of user", user.getEmail());
        } else throw new CustomValidationException("Old password doesn't match");
    }

    private String generatePassword() {
        SecureRandom random = new SecureRandom();
        return String.format("%06d", random.nextInt(1000000));
    }
}