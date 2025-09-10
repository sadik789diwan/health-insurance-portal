package com.insurance.hip.service;

import com.insurance.hip.dto.AuthResponse;
import com.insurance.hip.service.jwt.JwtService;
import com.insurance.hip.model.*;
import com.insurance.hip.repository.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserRepository userRepo;
    private final RefreshTokenRepository refreshTokenRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authManager, JwtService jwtService,
                       UserRepository userRepo, RefreshTokenRepository refreshTokenRepo,
                       BCryptPasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.userRepo = userRepo;
        this.refreshTokenRepo = refreshTokenRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // Authenticate and create tokens
    public AuthResponse authenticate(String username, String password) {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        User user = userRepo.findByUsername(username).orElseThrow();

        Map<String, Object> claims = Map.of("roles", user.getRoles());

        String accessToken = jwtService.generateAccessToken(username, claims);
        String refreshToken = jwtService.generateRefreshToken(username);

        // Persist refresh token
        RefreshToken rt = RefreshToken.builder()
                .token(refreshToken)
                .user(user)
                .expiryDate(Instant.now().plusMillis(Long.parseLong(System.getProperty("jwtRefreshMs", "604800000"))))
                .build();
        // But better use expiry from config; we will set from jwtService settings instead:
        rt.setExpiryDate(Instant.now().plusMillis(jwtService.extractExpiration(refreshToken).getTime() - System.currentTimeMillis()));
        refreshTokenRepo.save(rt);

        return new AuthResponse(accessToken, refreshToken);
    }

    // Create new access token from refresh token
    public AuthResponse refreshToken(String refreshToken) {
        RefreshToken tokenEntity = refreshTokenRepo.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (tokenEntity.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepo.delete(tokenEntity);
            throw new RuntimeException("Refresh token expired - please login again");
        }

        String username = tokenEntity.getUser().getUsername();
        Map<String, Object> claims = Map.of("roles", tokenEntity.getUser().getRoles());

        String newAccessToken = jwtService.generateAccessToken(username, claims);

        // Optionally: rotate refresh tokens — generate and save a new refresh token and delete old one
        // For simplicity, return same refresh token
        return new AuthResponse(newAccessToken, refreshToken);
    }

    // Revoke refresh tokens for user
    public void revokeRefreshTokens(User user) {
        refreshTokenRepo.deleteByUser(user);
    }

    // Create initial users (demo)
    public User createUserIfNotExists(String username, String rawPassword, java.util.Set<Role> roles) {
        return userRepo.findByUsername(username).orElseGet(() -> {
            User u = User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(rawPassword))
                    .roles(roles)
                    .build();
            return userRepo.save(u);
        });
    }
}
