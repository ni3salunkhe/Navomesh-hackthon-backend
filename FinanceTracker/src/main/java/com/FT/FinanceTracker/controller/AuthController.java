package com.FT.FinanceTracker.controller;

import com.FT.FinanceTracker.config.security.JwtUtil;
import com.FT.FinanceTracker.dto.AuthResponseDto;
import com.FT.FinanceTracker.dto.LoginRequestDto;
import com.FT.FinanceTracker.entity.User;
import com.FT.FinanceTracker.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final com.FT.FinanceTracker.service.SystemLogService logService;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil,
                          com.FT.FinanceTracker.service.SystemLogService logService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.logService = logService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseGet(() -> {
                    logService.warn("Login failed: User not found for email " + dto.getEmail(), "AUTH_CONTROLLER");
                    return null;
                });

        if (user == null) {
             return ResponseEntity.badRequest().body("Invalid credentials");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            logService.warn("Login failed: Invalid password for user " + dto.getEmail(), "AUTH_CONTROLLER");
            return ResponseEntity.badRequest().body("Invalid credentials");
        }

        logService.info("User logged in: " + user.getEmail() + " (" + user.getRole() + ")", "AUTH_CONTROLLER");

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        
        AuthResponseDto.AuthData authData = new AuthResponseDto.AuthData(
                token, 
                user.getFullName(), 
                user.getEmail(), 
                user.getRole().name()
        );
        
        return ResponseEntity.ok(new AuthResponseDto(true, authData));
    }
}
