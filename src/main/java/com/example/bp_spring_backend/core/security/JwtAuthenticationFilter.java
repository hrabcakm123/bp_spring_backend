package com.example.bp_spring_backend.core.security;

import com.example.bp_spring_backend.feature.user.UserEntity;
import com.example.bp_spring_backend.feature.enums.RoleEnum;
import com.example.bp_spring_backend.core.dto.ErrorResponseDTO;
import com.example.bp_spring_backend.core.exception.CustomValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final HelperAccessService helperAccessService;
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

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
                            .build();
                }

                if (jwtService.isTokenValid(jwtToken, userDetails)) {

                    if (RoleEnum.valueOf(userRole) == RoleEnum.HELPER) {
                        UserEntity user = (UserEntity) userDetailsService.loadUserByUsername(userEmail);
                        helperAccessService.checkLoginAllowed(user);
                    }

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
            log.error("JWT Token is expired");
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token expired"/*, List.of(ex.getMessage())*/);
            return;
        } catch (JwtException | IllegalArgumentException ex) {
            log.error("JWT Token is invalid");
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid token"/*, List.of(ex.getMessage())*/);
            return;
        } catch (CustomValidationException ex) {
            log.error(ex.getMessage());
            sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, ex.getMessage());
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
