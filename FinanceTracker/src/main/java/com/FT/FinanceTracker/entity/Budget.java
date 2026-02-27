package com.FT.FinanceTracker.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "budgets")
public class Budget extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String category;

    @Column(precision = 19, scale = 2)
    private BigDecimal limitAmount;

    @Enumerated(EnumType.STRING)
    private BudgetPeriod period;

    private LocalDate startDate;

    public enum BudgetPeriod {
        DAILY,
        WEEKLY,
        MONTHLY
    }

    public Budget() {
    }

    public Budget(UUID id, User user, String category, BigDecimal limitAmount, BudgetPeriod period, LocalDate startDate) {
        this.id = id;
        this.user = user;
        this.category = category;
        this.limitAmount = limitAmount;
        this.period = period;
        this.startDate = startDate;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getPeriodStart() {
        LocalDate now = LocalDate.now();
        switch (period) {
            case DAILY:
                return now;
            case WEEKLY:
                return now.with(java.time.DayOfWeek.MONDAY);
            case MONTHLY:
                return now.withDayOfMonth(1);
            default:
                return startDate != null ? startDate : now.withDayOfMonth(1);
        }
    }

    public LocalDate getPeriodEnd() {
        LocalDate start = getPeriodStart();
        switch (period) {
            case DAILY:
                return start;
            case WEEKLY:
                return start.plusDays(6);
            case MONTHLY:
                return start.plusMonths(1).minusDays(1);
            default:
                return start.plusMonths(1).minusDays(1);
        }
    }
}