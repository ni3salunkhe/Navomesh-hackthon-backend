package com.FT.FinanceTracker.utils;

import com.FT.FinanceTracker.dto.MerchantInfo;
import com.FT.FinanceTracker.dto.TransactionDto;
import com.FT.FinanceTracker.entity.Transaction;
import com.FT.FinanceTracker.entity.User;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public Transaction toEntity(TransactionDto dto, MerchantInfo merchantInfo,
                                 String category, double confidence, User user) {
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setTransactionDate(dto.getDate());
        transaction.setRawDescription(dto.getRawDescription());
        transaction.setAmount(dto.getAmount());
        transaction.setType(dto.getType());
        transaction.setBalance(dto.getBalance());
        transaction.setNormalizedMerchant(merchantInfo.getNormalizedName());
        transaction.setSystemCategory(category);
        transaction.setConfidenceScore(confidence);
        transaction.setModelVersion("v1.0");
        transaction.setRecurringFlag(false);
        return transaction;
    }

    public TransactionDto toDto(Transaction transaction) {
        TransactionDto dto = new TransactionDto();
        dto.setDate(transaction.getTransactionDate());
        dto.setRawDescription(transaction.getRawDescription());
        dto.setAmount(transaction.getAmount());
        dto.setType(transaction.getType());
        dto.setBalance(transaction.getBalance());
        dto.setRecurringFlag(transaction.getRecurringFlag());
        return dto;
    }
}
