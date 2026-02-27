package com.FT.FinanceTracker.entity;

import jakarta.persistence.*;
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

    private Double currentSpent;

    private Double limitAmount;

    private String message;

    @Enumerated(EnumType.STRING)
    private AlertStatus status;

    public enum AlertStatus {
        ACTIVE,
        READ
    }

    public Alert() {
    }

    public Alert(UUID id, User user, String category, Double currentSpent, Double limitAmount, String message, AlertStatus status) {
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

    public Double getCurrentSpent() {
        return currentSpent;
    }

    public void setCurrentSpent(Double currentSpent) {
        this.currentSpent = currentSpent;
    }

    public Double getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(Double limitAmount) {
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