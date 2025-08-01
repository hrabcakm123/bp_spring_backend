package com.example.bp_spring_backend.auth;

import com.example.bp_spring_backend.config.JwtService;
import com.example.bp_spring_backend.domains.inputDTO.AuthenticationRequestDTO;
import com.example.bp_spring_backend.domains.inputDTO.RegisterRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.AuthentificationResponseDTO;
import com.example.bp_spring_backend.domains.entity.UserEntity;
import com.example.bp_spring_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthentificationResponseDTO register(RegisterRequestDTO request) {
        var user = UserEntity.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roleEnum(request.getRoleEnum())
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthentificationResponseDTO.builder()
                .token(jwtToken)
                .build();
    }

    public AuthentificationResponseDTO authenticate(AuthenticationRequestDTO request) {
        authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
                  request.getEmail(),
                  request.getPassword()
          )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthentificationResponseDTO.builder()
                .token(jwtToken)
                .build();
    }
}
