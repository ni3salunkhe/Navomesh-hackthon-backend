package com.FT.FinanceTracker.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "recurring_payments")
public class RecurringPayment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String merchant;

    private Double averageAmount;

    private Integer intervalDays;

    private Double confidenceScore;

    public RecurringPayment() {
    }

    public RecurringPayment(UUID id, User user, String merchant, Double averageAmount, Integer intervalDays, Double confidenceScore) {
        this.id = id;
        this.user = user;
        this.merchant = merchant;
        this.averageAmount = averageAmount;
        this.intervalDays = intervalDays;
        this.confidenceScore = confidenceScore;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public Double getAverageAmount() {
        return averageAmount;
    }

    public void setAverageAmount(Double averageAmount) {
        this.averageAmount = averageAmount;
    }

    public Integer getIntervalDays() {
        return intervalDays;
    }

    public void setIntervalDays(Integer intervalDays) {
        this.intervalDays = intervalDays;
    }

    public Double getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(Double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }
}