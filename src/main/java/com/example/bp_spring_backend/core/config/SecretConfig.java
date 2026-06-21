package com.example.bp_spring_backend.core.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SecretConfig {

    @Value("${app.secret-key}")
    private String secretKey;
}
