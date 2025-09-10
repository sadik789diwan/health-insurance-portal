package com.insurance.hip.init;

import com.insurance.hip.model.Role;
import com.insurance.hip.model.User;
import com.insurance.hip.service.AuthService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer {

    private final AuthService authService;

    public DataInitializer(AuthService authService) { this.authService = authService; }

    @PostConstruct
    public void init() {
        authService.createUserIfNotExists("alice", "password", Set.of(Role.ROLE_USER));
        authService.createUserIfNotExists("agent1", "password", Set.of(Role.ROLE_AGENT));
        authService.createUserIfNotExists("admin", "password", Set.of(Role.ROLE_ADMIN));
        authService.createUserIfNotExists("uw", "password", Set.of(Role.ROLE_UNDERWRITER));
        authService.createUserIfNotExists("auditor", "password", Set.of(Role.ROLE_AUDITOR));
    }
}
