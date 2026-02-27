package com.FT.FinanceTracker.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DashboardResponseDto {

    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal netBalance;
    private long transactionCount;
    private Map<String, BigDecimal> categoryBreakdown;
    private List<TransactionDto> recentTransactions;
    private List<RecurringDto> recurringPayments;
    private List<AlertDto> alerts;
    private long reviewCount;

    public DashboardResponseDto() {
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
    }

    public BigDecimal getNetBalance() {
        return netBalance;
    }

    public void setNetBalance(BigDecimal netBalance) {
        this.netBalance = netBalance;
    }

    public long getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(long transactionCount) {
        this.transactionCount = transactionCount;
    }

    public Map<String, BigDecimal> getCategoryBreakdown() {
        return categoryBreakdown;
    }

    public void setCategoryBreakdown(Map<String, BigDecimal> categoryBreakdown) {
        this.categoryBreakdown = categoryBreakdown;
    }

    public List<TransactionDto> getRecentTransactions() {
        return recentTransactions;
    }

    public void setRecentTransactions(List<TransactionDto> recentTransactions) {
        this.recentTransactions = recentTransactions;
    }

    public List<RecurringDto> getRecurringPayments() {
        return recurringPayments;
    }

    public void setRecurringPayments(List<RecurringDto> recurringPayments) {
        this.recurringPayments = recurringPayments;
    }

    public List<AlertDto> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<AlertDto> alerts) {
        this.alerts = alerts;
    }

    public long getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(long reviewCount) {
        this.reviewCount = reviewCount;
    }
}
