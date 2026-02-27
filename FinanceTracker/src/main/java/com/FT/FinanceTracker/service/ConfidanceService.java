package com.FT.FinanceTracker.service;

import com.FT.FinanceTracker.dto.MerchantInfo;
import com.FT.FinanceTracker.dto.TransactionDto;

public interface ConfidanceService {
	double calculate(TransactionDto dto, MerchantInfo merchantInfo, String category);
}
