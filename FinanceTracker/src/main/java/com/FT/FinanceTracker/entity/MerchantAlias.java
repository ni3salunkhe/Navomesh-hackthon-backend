package com.FT.FinanceTracker.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "merchant_alias")
public class MerchantAlias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pattern;

    private String normalizedName;

    private String defaultCategory;

    private Integer priority;

    public MerchantAlias() {
    }

    public MerchantAlias(Long id, String pattern, String normalizedName, String defaultCategory, Integer priority) {
        this.id = id;
        this.pattern = pattern;
        this.normalizedName = normalizedName;
        this.defaultCategory = defaultCategory;
        this.priority = priority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    public String getDefaultCategory() {
        return defaultCategory;
    }

    public void setDefaultCategory(String defaultCategory) {
        this.defaultCategory = defaultCategory;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
