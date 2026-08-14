package com.renatoboranga.gymflow.config;

import jakarta.validation.constraints.Email;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties("gymflow.security.bootstrap")
public record AdminBootstrapProperties(@Email String adminEmail, String adminPassword) {
}
