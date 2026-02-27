package com.FT.FinanceTracker.service;

import com.FT.FinanceTracker.dto.MerchantInfo;

public interface MerchantNormalizationService {
	MerchantInfo normalize(String rawDescription);
}
