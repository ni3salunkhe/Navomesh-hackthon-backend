package com.FT.FinanceTracker.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "alerts")
public class Alert extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String category;

    @Column(precision = 19, scale = 2)
    private BigDecimal currentSpent;

    @Column(precision = 19, scale = 2)
    private BigDecimal limitAmount;

    private String message;

    @Enumerated(EnumType.STRING)
    private AlertStatus status;

    public enum AlertStatus {
        ACTIVE,
        READ
    }

    public Alert() {
    }

    public Alert(UUID id, User user, String category, BigDecimal currentSpent, BigDecimal limitAmount, String message, AlertStatus status) {
        this.id = id;
        this.user = user;
        this.category = category;
        this.currentSpent = currentSpent;
        this.limitAmount = limitAmount;
        this.message = message;
        this.status = status;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AlertStatus getStatus() {
        return status;
    }

    public void setStatus(AlertStatus status) {
        this.status = status;
    }
}