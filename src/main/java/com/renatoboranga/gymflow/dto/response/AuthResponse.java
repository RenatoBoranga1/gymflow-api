package com.renatoboranga.gymflow.dto.response;

import com.renatoboranga.gymflow.model.Role;

public record AuthResponse(
        String accessToken,
        String tokenType,
        long expiresIn,
        Role role) {
}
