package com.findsure.service;

import com.findsure.dto.AuthResponse;
import com.findsure.dto.LoginRequest;
import com.findsure.dto.RegisterRequest;
import com.findsure.dto.UserResponse;
import com.findsure.entity.User;
import com.findsure.exception.ConflictException;
import com.findsure.exception.UnauthorizedException;
import com.findsure.repository.UserRepository;
import com.findsure.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public UserResponse register(RegisterRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new ConflictException("An account with this email already exists.");
        }

        User user = User.builder()
                .name(request.getName().trim())
                .email(normalizedEmail)
                .phone(request.getPhone())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .status(User.Status.active)
                .build();

        User saved = userRepository.save(user);
        return UserResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid email or password.");
        }

        if (user.getStatus() != User.Status.active) {
            throw new UnauthorizedException("This account is not active.");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getEmail());
        return new AuthResponse(token, jwtUtil.getExpirationSeconds(), UserResponse.from(user));
    }
}
