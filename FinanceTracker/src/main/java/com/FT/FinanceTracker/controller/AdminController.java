package com.FT.FinanceTracker.controller;

import com.FT.FinanceTracker.entity.SystemLog;
import com.FT.FinanceTracker.repository.SystemLogRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final SystemLogRepository logRepository;

    public AdminController(SystemLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @GetMapping("/logs")
    public List<SystemLog> getLogs() {
        return logRepository.findAll();
    }
}
