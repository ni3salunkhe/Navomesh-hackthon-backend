package com.FT.FinanceTracker.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
    name = "transactions",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {
            "user_id",
            "transactionDate",
            "amount",
            "normalizedMerchant"
        }
    )
)
public class Transaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDate transactionDate;

    @Column(length = 1000)
    private String rawDescription;

    @Column(precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(precision = 19, scale = 2)
    private BigDecimal balance;

    // System-generated intelligence
    private String normalizedMerchant;

    private String systemCategory;

    private Double confidenceScore;

    private String modelVersion;

    // Assisted intelligence override
    private String userOverrideCategory;

    private String userOverrideMerchant;

    private Boolean recurringFlag;

    public enum TransactionType {
        DEBIT,
        CREDIT
    }

    public Transaction() {
    }

    public Transaction(UUID id, User user, LocalDate transactionDate, String rawDescription, BigDecimal amount,
                       TransactionType type, BigDecimal balance, String normalizedMerchant, String systemCategory,
                       Double confidenceScore, String modelVersion, String userOverrideCategory,
                       String userOverrideMerchant, Boolean recurringFlag) {
        this.id = id;
        this.user = user;
        this.transactionDate = transactionDate;
        this.rawDescription = rawDescription;
        this.amount = amount;
        this.type = type;
        this.balance = balance;
        this.normalizedMerchant = normalizedMerchant;
        this.systemCategory = systemCategory;
        this.confidenceScore = confidenceScore;
        this.modelVersion = modelVersion;
        this.userOverrideCategory = userOverrideCategory;
        this.userOverrideMerchant = userOverrideMerchant;
        this.recurringFlag = recurringFlag;
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

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
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

    public String getNormalizedMerchant() {
        return normalizedMerchant;
    }

    public void setNormalizedMerchant(String normalizedMerchant) {
        this.normalizedMerchant = normalizedMerchant;
    }

    public String getSystemCategory() {
        return systemCategory;
    }

    public void setSystemCategory(String systemCategory) {
        this.systemCategory = systemCategory;
    }

    public Double getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(Double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getUserOverrideCategory() {
        return userOverrideCategory;
    }

    public void setUserOverrideCategory(String userOverrideCategory) {
        this.userOverrideCategory = userOverrideCategory;
    }

    public String getUserOverrideMerchant() {
        return userOverrideMerchant;
    }

    public void setUserOverrideMerchant(String userOverrideMerchant) {
        this.userOverrideMerchant = userOverrideMerchant;
    }

    public Boolean getRecurringFlag() {
        return recurringFlag;
    }

    public void setRecurringFlag(Boolean recurringFlag) {
        this.recurringFlag = recurringFlag;
    }
}