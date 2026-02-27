package com.FT.FinanceTracker.dto;

public class TransactionOverrideDto {

    private String category;
    private String merchant;
    private java.math.BigDecimal amount;

    public TransactionOverrideDto() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public java.math.BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(java.math.BigDecimal amount) {
        this.amount = amount;
    }
}
