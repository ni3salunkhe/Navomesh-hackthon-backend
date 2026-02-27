package com.FT.FinanceTracker.service;

import java.util.List;

import com.FT.FinanceTracker.dto.TransactionDto;

public interface TransactionExtractionService {
	List<TransactionDto> extractTransactions(String rawText);
}
