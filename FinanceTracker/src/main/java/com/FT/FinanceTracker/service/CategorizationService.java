package com.FT.FinanceTracker.service;

import com.FT.FinanceTracker.dto.MerchantInfo;
import com.FT.FinanceTracker.dto.TransactionDto;

public interface CategorizationService {
	String categorize(TransactionDto dto, MerchantInfo merchantInfo);
}
