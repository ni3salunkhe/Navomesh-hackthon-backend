package com.FT.FinanceTracker.dto;

import java.math.BigDecimal;

public class RecurringDto {

    private String merchant;
    private BigDecimal averageAmount;
    private int intervalDays;
    private double confidence;

    public RecurringDto() {
    }

    public RecurringDto(String merchant, BigDecimal averageAmount, int intervalDays, double confidence) {
        this.merchant = merchant;
        this.averageAmount = averageAmount;
        this.intervalDays = intervalDays;
        this.confidence = confidence;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public BigDecimal getAverageAmount() {
        return averageAmount;
    }

    public void setAverageAmount(BigDecimal averageAmount) {
        this.averageAmount = averageAmount;
    }

    public int getIntervalDays() {
        return intervalDays;
    }

    public void setIntervalDays(int intervalDays) {
        this.intervalDays = intervalDays;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
}
