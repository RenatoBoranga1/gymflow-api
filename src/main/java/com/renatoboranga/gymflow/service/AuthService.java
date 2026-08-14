package com.renatoboranga.gymflow.service;

import com.renatoboranga.gymflow.dto.request.LoginRequest;
import com.renatoboranga.gymflow.dto.request.RegisterRequest;
import com.renatoboranga.gymflow.dto.response.AuthResponse;
import com.renatoboranga.gymflow.exception.ConflictException;
import com.renatoboranga.gymflow.model.Role;
import com.renatoboranga.gymflow.model.UserAccount;
import com.renatoboranga.gymflow.repository.UserAccountRepository;
import com.renatoboranga.gymflow.security.JwtTokenService;
import java.util.Locale;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserAccountRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService tokenService;

    public AuthService(
            UserAccountRepository repository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtTokenService tokenService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = normalize(request.email());
        if (repository.existsByEmailIgnoreCase(email)) {
            throw new ConflictException("Já existe uma conta com este e-mail");
        }
        UserAccount account = repository.save(new UserAccount(
                email,
                passwordEncoder.encode(request.password()),
                Role.USER));
        return tokenService.issue(account.getEmail(), account.getRole());
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String email = normalize(request.email());
        authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(email, request.password()));
        UserAccount account = repository.findByEmailIgnoreCase(email).orElseThrow();
        return tokenService.issue(account.getEmail(), account.getRole());
    }

    private String normalize(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
