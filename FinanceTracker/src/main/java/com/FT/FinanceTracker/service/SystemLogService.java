package com.FT.FinanceTracker.service;

public interface SystemLogService {
    void log(String level, String message, String source);
    void info(String message, String source);
    void warn(String message, String source);
    void error(String message, String source);
}
