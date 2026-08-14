package com.renatoboranga.gymflow.security;

import com.renatoboranga.gymflow.config.AdminBootstrapProperties;
import com.renatoboranga.gymflow.model.Role;
import com.renatoboranga.gymflow.model.UserAccount;
import com.renatoboranga.gymflow.repository.UserAccountRepository;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
public class AdminBootstrap implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminBootstrap.class);

    private final AdminBootstrapProperties properties;
    private final UserAccountRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AdminBootstrap(
            AdminBootstrapProperties properties,
            UserAccountRepository repository,
            PasswordEncoder passwordEncoder) {
        this.properties = properties;
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        boolean hasEmail = StringUtils.hasText(properties.adminEmail());
        boolean hasPassword = StringUtils.hasText(properties.adminPassword());
        if (!hasEmail && !hasPassword) {
            return;
        }
        if (!hasEmail || !hasPassword) {
            throw new IllegalStateException(
                    "ADMIN_EMAIL e ADMIN_PASSWORD devem ser informados em conjunto");
        }
        if (properties.adminPassword().length() < 12 || properties.adminPassword().length() > 72) {
            throw new IllegalStateException("ADMIN_PASSWORD deve ter entre 12 e 72 caracteres");
        }
        String email = properties.adminEmail().trim().toLowerCase(Locale.ROOT);
        UserAccount existing = repository.findByEmailIgnoreCase(email).orElse(null);
        if (existing != null && existing.getRole() != Role.ADMIN) {
            throw new IllegalStateException(
                    "ADMIN_EMAIL já pertence a uma conta sem perfil administrativo");
        }
        if (existing != null) {
            return;
        }
        repository.save(new UserAccount(
                email,
                passwordEncoder.encode(properties.adminPassword()),
                Role.ADMIN));
        LOGGER.info("Conta administrativa inicial criada a partir de configuração externa");
    }
}
