package com.FT.FinanceTracker.dto;

public class MerchantInfo {

    private String normalizedName;
    private String category;
    private double confidenceScore;

    public MerchantInfo() {
    }

    public MerchantInfo(String normalizedName, String category, double confidenceScore) {
        this.normalizedName = normalizedName;
        this.category = category;
        this.confidenceScore = confidenceScore;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }
}
