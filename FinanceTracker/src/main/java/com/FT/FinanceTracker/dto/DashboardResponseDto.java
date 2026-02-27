package com.FT.FinanceTracker.dto;

import java.util.List;
import java.util.Map;

public class DashboardResponseDto {

    private double totalIncome;
    private double totalExpense;
    private double netBalance;
    private long transactionCount;
    private Map<String, Double> categoryBreakdown;
    private List<TransactionDto> recentTransactions;
    private List<RecurringDto> recurringPayments;

    public DashboardResponseDto() {
    }

    public DashboardResponseDto(double totalIncome, double totalExpense, double netBalance, long transactionCount, Map<String, Double> categoryBreakdown, List<TransactionDto> recentTransactions) {
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.netBalance = netBalance;
        this.transactionCount = transactionCount;
        this.categoryBreakdown = categoryBreakdown;
        this.recentTransactions = recentTransactions;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
    }

    public double getNetBalance() {
        return netBalance;
    }

    public void setNetBalance(double netBalance) {
        this.netBalance = netBalance;
    }

    public long getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(long transactionCount) {
        this.transactionCount = transactionCount;
    }

    public Map<String, Double> getCategoryBreakdown() {
        return categoryBreakdown;
    }

    public void setCategoryBreakdown(Map<String, Double> categoryBreakdown) {
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
}
