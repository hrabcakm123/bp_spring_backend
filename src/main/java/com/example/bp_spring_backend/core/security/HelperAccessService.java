package com.example.bp_spring_backend.core.security;

import com.example.bp_spring_backend.feature.user.UserEntity;
import com.example.bp_spring_backend.core.exception.CustomValidationException;
import com.example.bp_spring_backend.feature.userExercise.UserExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class HelperAccessService {

    private final UserExerciseRepository userExerciseRepository;

    public void checkLoginAllowed(UserEntity user) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        boolean hasSessionToday = userExerciseRepository.existsByUserIdAndSessionDate(user.getId(), today);

        if (!hasSessionToday) {
            throw new CustomValidationException("HELPER can login only on the day of assigned exercise");
        }

        if (now.isBefore(LocalTime.of(7, 0)) || now.isAfter(LocalTime.of(19, 0))) {
            throw new CustomValidationException("Login allowed only between 07:00 and 19:00");
        }
    }
}
