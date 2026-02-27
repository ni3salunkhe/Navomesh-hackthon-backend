package com.FT.FinanceTracker.dto;

public class SystemStatsDto {
    private long totalUsers;
    private long totalTransactions;
    private long totalLogs;
    private double systemHealth; // 0.0 to 1.0

    public SystemStatsDto() {}

    public SystemStatsDto(long totalUsers, long totalTransactions, long totalLogs, double systemHealth) {
        this.totalUsers = totalUsers;
        this.totalTransactions = totalTransactions;
        this.totalLogs = totalLogs;
        this.systemHealth = systemHealth;
    }

    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }

    public long getTotalTransactions() { return totalTransactions; }
    public void setTotalTransactions(long totalTransactions) { this.totalTransactions = totalTransactions; }

    public long getTotalLogs() { return totalLogs; }
    public void setTotalLogs(long totalLogs) { this.totalLogs = totalLogs; }

    public double getSystemHealth() { return systemHealth; }
    public void setSystemHealth(double systemHealth) { this.systemHealth = systemHealth; }
}
