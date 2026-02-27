package com.FT.FinanceTracker.service;

import com.FT.FinanceTracker.dto.UserRegistrationDto;
import com.FT.FinanceTracker.dto.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserRegistrationDto dto);
    UserResponseDto getByEmail(String email);
}
