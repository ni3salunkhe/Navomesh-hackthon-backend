package com.FT.FinanceTracker.dto;

import com.FT.FinanceTracker.entity.Transaction.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionDto {

    private LocalDate date;
    private String rawDescription;
    private BigDecimal amount;
    private TransactionType type;
    private BigDecimal balance;
    
    // Enriched fields
    private String merchant;
    private String category;
    private Double confidence;
    private Boolean needsReview;
    private Boolean recurringFlag;

    public TransactionDto() {
    }

    public TransactionDto(LocalDate date, String rawDescription, BigDecimal amount, TransactionType type, BigDecimal balance) {
        this.date = date;
        this.rawDescription = rawDescription;
        this.amount = amount;
        this.type = type;
        this.balance = balance;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getRawDescription() {
        return rawDescription;
    }

    public void setRawDescription(String rawDescription) {
        this.rawDescription = rawDescription;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public Boolean getNeedsReview() {
        return needsReview;
    }

    public void setNeedsReview(Boolean needsReview) {
        this.needsReview = needsReview;
    }

    public Boolean getRecurringFlag() {
        return recurringFlag;
    }

    public void setRecurringFlag(Boolean recurringFlag) {
        this.recurringFlag = recurringFlag;
    }
}
