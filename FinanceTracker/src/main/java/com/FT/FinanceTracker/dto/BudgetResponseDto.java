package com.FT.FinanceTracker.dto;

import java.math.BigDecimal;
import java.util.UUID;
import com.FT.FinanceTracker.entity.Budget.BudgetPeriod;

public class BudgetResponseDto {

    private UUID id;
    private String category;
    private BigDecimal limitAmount;
    private BudgetPeriod period;
    private BigDecimal currentSpent;

    public BudgetResponseDto() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }

    public BudgetPeriod getPeriod() {
        return period;
    }

    public void setPeriod(BudgetPeriod period) {
        this.period = period;
    }

    public BigDecimal getCurrentSpent() {
        return currentSpent;
    }

    public void setCurrentSpent(BigDecimal currentSpent) {
        this.currentSpent = currentSpent;
    }
}
