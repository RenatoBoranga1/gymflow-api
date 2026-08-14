package com.renatoboranga.gymflow.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import org.junit.jupiter.api.Test;

class SecurityConfigTest {

    private final SecurityConfig config = new SecurityConfig();

    @Test
    void rejeitaSegredoAusenteOuCurto() {
        assertThatThrownBy(() -> config.jwtSecretKey(
                new JwtProperties("", "gymflow-api", Duration.ofMinutes(15))))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("256 bits");
    }

    @Test
    void rejeitaSegredoQueNaoEstaEmBase64() {
        assertThatThrownBy(() -> config.jwtSecretKey(
                new JwtProperties("%%%", "gymflow-api", Duration.ofMinutes(15))))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Base64");
    }

    @Test
    void aceitaSegredoAleatorioCom256Bits() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        JwtProperties properties = new JwtProperties(
                Base64.getEncoder().encodeToString(bytes),
                "gymflow-api",
                Duration.ofMinutes(15));

        assertThat(config.jwtSecretKey(properties).getEncoded()).hasSize(32);
    }
}
