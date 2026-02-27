package com.FT.FinanceTracker.dto;

import java.math.BigDecimal;

public class AlertDto {

    private String category;
    private BigDecimal currentSpent;
    private BigDecimal limitAmount;
    private String status;
    private String message;
    private String type;
    private String severity;

    public AlertDto() {
    }

    public AlertDto(String category, BigDecimal currentSpent, BigDecimal limitAmount, String status, String message, String type, String severity) {
        this.category = category;
        this.currentSpent = currentSpent;
        this.limitAmount = limitAmount;
        this.status = status;
        this.message = message;
        this.type = type;
        this.severity = severity;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }
}
