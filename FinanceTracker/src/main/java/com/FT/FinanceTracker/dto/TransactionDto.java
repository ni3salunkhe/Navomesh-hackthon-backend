package com.FT.FinanceTracker.dto;

import com.FT.FinanceTracker.entity.Transaction.TransactionType;
import java.time.LocalDate;

public class TransactionDto {

    private LocalDate date;
    private String rawDescription;
    private Double amount;
    private TransactionType type;
    private Double balance;

    public TransactionDto() {
    }

    public TransactionDto(LocalDate date, String rawDescription, Double amount, TransactionType type, Double balance) {
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
