package com.FT.FinanceTracker.dto;

import java.math.BigDecimal;
import com.FT.FinanceTracker.entity.Budget.BudgetPeriod;

public class BudgetRequestDto {

    private String category;
    private BigDecimal limitAmount;
    private BudgetPeriod period;

    public BudgetRequestDto() {
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
}
