package com.FT.FinanceTracker.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(
    name = "recurring_payments",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"user_id", "merchant"}
    )
)
public class RecurringPayment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String merchant;

    @Column(precision = 19, scale = 2)
    private BigDecimal averageAmount;

    private Integer intervalDays;

    private Double confidenceScore;

    public RecurringPayment() {
    }

    public RecurringPayment(UUID id, User user, String merchant, BigDecimal averageAmount, Integer intervalDays, Double confidenceScore) {
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

    public BigDecimal getAverageAmount() {
        return averageAmount;
    }

    public void setAverageAmount(BigDecimal averageAmount) {
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