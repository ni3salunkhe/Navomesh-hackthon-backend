package com.FT.FinanceTracker.controller;

import com.FT.FinanceTracker.entity.SystemLog;
import com.FT.FinanceTracker.repository.SystemLogRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final com.FT.FinanceTracker.repository.SystemLogRepository logRepository;
    private final com.FT.FinanceTracker.repository.UserRepository userRepository;
    private final com.FT.FinanceTracker.repository.TransactionRepository transactionRepository;

    public AdminController(com.FT.FinanceTracker.repository.SystemLogRepository logRepository,
                           com.FT.FinanceTracker.repository.UserRepository userRepository,
                           com.FT.FinanceTracker.repository.TransactionRepository transactionRepository) {
        this.logRepository = logRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/stats")
    public com.FT.FinanceTracker.dto.SystemStatsDto getStats() {
        long totalUsers = userRepository.count();
        long totalTransactions = transactionRepository.count();
        long totalLogs = logRepository.count();
        
        // Simple health metric: Logs/Users ratio (just as a placeholder for real health)
        double health = 0.98; 
        
        return new com.FT.FinanceTracker.dto.SystemStatsDto(totalUsers, totalTransactions, totalLogs, health);
    }

    @GetMapping("/users")
    public List<com.FT.FinanceTracker.dto.UserAdminDto> getUsers() {
        return userRepository.findAll().stream()
                .map(u -> new com.FT.FinanceTracker.dto.UserAdminDto(
                        u.getId(),
                        u.getEmail(),
                        u.getFullName(),
                        u.getRole(),
                        u.getStatus(),
                        u.getCreatedAt()
                ))
                .collect(java.util.stream.Collectors.toList());
    }

    @PatchMapping("/users/{id}/status")
    public void updateUserStatus(@PathVariable java.util.UUID id, @RequestBody java.util.Map<String, String> body) {
        com.FT.FinanceTracker.entity.User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        String statusStr = body.get("status");
        if (statusStr != null) {
            user.setStatus(com.FT.FinanceTracker.entity.User.Status.valueOf(statusStr));
            userRepository.save(user);
        }
    }

    @GetMapping("/logs")
    public List<com.FT.FinanceTracker.entity.SystemLog> getLogs() {
        return logRepository.findAll();
    }
}
