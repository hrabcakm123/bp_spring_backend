package com.example.bp_spring_backend.auth;

import com.example.bp_spring_backend.config.HelperAccessService;
import com.example.bp_spring_backend.config.JwtService;
import com.example.bp_spring_backend.domains.enums.RoleEnum;
import com.example.bp_spring_backend.domains.inputDTO.AuthenticationRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.AuthenticationResponseDTO;
import com.example.bp_spring_backend.exception.CustomValidationException;
import com.example.bp_spring_backend.exception.UserNotFoundException;
import com.example.bp_spring_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final HelperAccessService helperAccessService;

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(""));
        if (user.getRoleEnum().equals(RoleEnum.SYSTEM)) {
            throw new CustomValidationException("SYSTEM user cannot login");
        }
        if (user.getRoleEnum().equals(RoleEnum.HELPER)) {
            helperAccessService.checkLoginAllowed(user);
        }
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseDTO.builder()
                .token(jwtToken)
                .build();
    }
}
