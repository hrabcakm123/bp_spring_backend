package com.example.bp_spring_backend.config;

import com.example.bp_spring_backend.domains.entity.UserEntity;
import com.example.bp_spring_backend.domains.enums.RoleEnum;
import com.example.bp_spring_backend.domains.outputDTO.ErrorResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    // do not control token for following endpoints
    private final List<String> permitAllEndpoints = List.of(
            "/api/v1/auth/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/openapi.yml"
    );

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        boolean isPermitAll = permitAllEndpoints.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, requestURI));

        if (isPermitAll) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwtToken = authHeader.substring(7);

        try {
            final String userEmail = jwtService.extractUsername(jwtToken);
            final String userRole = jwtService.extractRole(jwtToken);
            final Integer userId = jwtService.extractId(jwtToken);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails;

                boolean loadFromDb = false;

                if (loadFromDb) {
                    userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                } else {
                    userDetails = UserEntity.builder()
                            .id(userId)
                            .email(userEmail)
                            .roleEnum(RoleEnum.valueOf(userRole))
                            .password("")
                            .firstname("")
                            .lastname("")
                            .build();
                }

                if (jwtService.isTokenValid(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException ex) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token expired"/*, List.of(ex.getMessage())*/);
            return;
        } catch (JwtException | IllegalArgumentException ex) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid token"/*, List.of(ex.getMessage())*/);
            return;
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message/*, List<String> errors*/) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");

        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        errorResponse.setStatus(status);
        errorResponse.setMessage(message);
        //errorResponse.setErrors(errors);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(errorResponse);

        PrintWriter writer = response.getWriter();
        writer.write(json);
        writer.flush();
    }
}
