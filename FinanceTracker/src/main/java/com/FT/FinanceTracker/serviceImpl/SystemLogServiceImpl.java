package com.FT.FinanceTracker.serviceImpl;

import com.FT.FinanceTracker.entity.SystemLog;
import com.FT.FinanceTracker.repository.SystemLogRepository;
import com.FT.FinanceTracker.service.SystemLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SystemLogServiceImpl implements SystemLogService {

    private final SystemLogRepository logRepository;

    public SystemLogServiceImpl(SystemLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    @Transactional
    public void log(String level, String message, String source) {
        SystemLog log = new SystemLog();
        log.setLevel(level);
        log.setMessage(message);
        log.setSource(source);
        logRepository.save(log);
    }

    @Override
    public void info(String message, String source) {
        log("INFO", message, source);
    }

    @Override
    public void warn(String message, String source) {
        log("WARN", message, source);
    }

    @Override
    public void error(String message, String source) {
        log("ERROR", message, source);
    }
}
