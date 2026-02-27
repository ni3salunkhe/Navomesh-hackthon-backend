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

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDto register(UserRegistrationDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already registered: " + dto.getEmail());
        }

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword()); // TODO: hash with BCrypt in production
        user.setRole(User.Role.USER);
        user.setStatus(User.Status.ACTIVE);

        User saved = userRepository.save(user);

        return new UserResponseDto(saved.getId(), saved.getEmail(), saved.getRole().name());
    }

    public UserResponseDto getByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
        return new UserResponseDto(user.getId(), user.getEmail(), user.getRole().name());
    }
}
