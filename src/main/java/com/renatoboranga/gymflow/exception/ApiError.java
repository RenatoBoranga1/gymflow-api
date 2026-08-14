package com.renatoboranga.gymflow.exception;

import java.time.Instant;
import java.util.Map;

public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> violations) {

    public ApiError {
        violations = violations == null ? Map.of() : Map.copyOf(violations);
    }
}
