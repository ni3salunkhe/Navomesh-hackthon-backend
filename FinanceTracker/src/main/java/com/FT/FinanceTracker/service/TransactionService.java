package com.FT.FinanceTracker.service;

import java.util.UUID;
import com.FT.FinanceTracker.dto.TransactionOverrideDto;

import java.util.List;
import com.FT.FinanceTracker.entity.Transaction;
import com.FT.FinanceTracker.entity.User;

public interface TransactionService {
    void overrideTransaction(UUID transactionId, TransactionOverrideDto dto);
    List<Transaction> getTransactionsForReview(User user);
}
