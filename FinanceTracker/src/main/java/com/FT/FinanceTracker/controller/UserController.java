package com.FT.FinanceTracker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.FT.FinanceTracker.dto.AuthResponseDto;
import com.FT.FinanceTracker.dto.UserRegistrationDto;
import com.FT.FinanceTracker.dto.UserResponseDto;
import com.FT.FinanceTracker.service.UserService;
import com.FT.FinanceTracker.config.security.JwtUtil;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationDto dto) {
        try {
            UserResponseDto userResponse = userService.register(dto);
            String token = jwtUtil.generateToken(userResponse.getEmail(), userResponse.getRole());
            
            AuthResponseDto.AuthData authData = new AuthResponseDto.AuthData(
                token,
                userResponse.getName(),
                userResponse.getEmail(),
                userResponse.getRole()
            );
            
            return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponseDto(true, authData));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        try {
            String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
            UserResponseDto response = userService.getByEmail(email);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(java.util.Map.of("error", e.getMessage()));
        }
    }
}
