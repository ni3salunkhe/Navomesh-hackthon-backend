package com.FT.FinanceTracker.serviceImpl;

import org.springframework.stereotype.Service;

import com.FT.FinanceTracker.dto.UserRegistrationDto;
import com.FT.FinanceTracker.dto.UserResponseDto;
import com.FT.FinanceTracker.entity.User;
import com.FT.FinanceTracker.repository.UserRepository;

import com.FT.FinanceTracker.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, 
                           org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDto register(UserRegistrationDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already registered: " + dto.getEmail());
        }

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFullName(dto.getName());
        user.setRole(User.Role.USER);
        user.setStatus(User.Status.ACTIVE);

        User saved = userRepository.save(user);

        return new UserResponseDto(saved.getId(), saved.getEmail(), saved.getRole().name(), saved.getFullName());
    }

    public UserResponseDto getByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
        return new UserResponseDto(user.getId(), user.getEmail(), user.getRole().name(), user.getFullName());
    }
}
