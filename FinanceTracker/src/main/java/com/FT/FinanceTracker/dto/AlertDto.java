package com.FT.FinanceTracker.dto;

import java.math.BigDecimal;

public class AlertDto {

    private String category;
    private BigDecimal currentSpent;
    private BigDecimal limitAmount;
    private String status;

    public AlertDto() {
    }

    public AlertDto(String category, BigDecimal currentSpent, BigDecimal limitAmount, String status) {
        this.category = category;
        this.currentSpent = currentSpent;
        this.limitAmount = limitAmount;
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getCurrentSpent() {
        return currentSpent;
    }

    public void setCurrentSpent(BigDecimal currentSpent) {
        this.currentSpent = currentSpent;
    }

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
