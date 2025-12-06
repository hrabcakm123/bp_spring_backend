package com.example.bp_spring_backend.auth;

import com.example.bp_spring_backend.domains.outputDTO.ErrorResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .status(HttpServletResponse.SC_UNAUTHORIZED)
                //.message(authException.getMessage())
                .message("Unauthenticated: Authentication is required to access this resource.")
                .build();

        String json = objectMapper.writeValueAsString(errorResponse);

        try (PrintWriter writer = response.getWriter()) {
            writer.write(json);
        }
    }
}
