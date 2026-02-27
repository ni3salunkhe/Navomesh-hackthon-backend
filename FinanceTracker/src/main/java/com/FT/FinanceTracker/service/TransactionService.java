package com.FT.FinanceTracker.service;

import java.util.UUID;
import com.FT.FinanceTracker.dto.TransactionOverrideDto;

public interface TransactionService {
    void overrideTransaction(UUID transactionId, TransactionOverrideDto dto);
}
