package com.renatoboranga.gymflow.security;

import com.renatoboranga.gymflow.model.UserAccount;
import com.renatoboranga.gymflow.repository.UserAccountRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GymFlowUserDetailsService implements UserDetailsService {

    private final UserAccountRepository repository;

    public GymFlowUserDetailsService(UserAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserAccount account = repository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Credenciais inválidas"));
        return User.withUsername(account.getEmail())
                .password(account.getPasswordHash())
                .roles(account.getRole().name())
                .build();
    }
}
