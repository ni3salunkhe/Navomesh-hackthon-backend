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

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }

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
