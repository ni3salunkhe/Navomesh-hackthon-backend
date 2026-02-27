package com.FT.FinanceTracker.serviceImpl;

import org.springframework.stereotype.Service;

import com.FT.FinanceTracker.dto.MerchantInfo;
import com.FT.FinanceTracker.dto.TransactionDto;
import com.FT.FinanceTracker.service.CategorizationService;

@Service
public class CategorizationServiceImpl implements CategorizationService {

    @Override
    public String categorize(TransactionDto dto, MerchantInfo merchantInfo) {
        if (!merchantInfo.getCategory().equals("Uncategorized"))
            return merchantInfo.getCategory();

        if (dto.getRawDescription() != null && dto.getRawDescription().contains("ATM"))
            return "Cash Withdrawal";

        if (dto.getType() != null && dto.getType() == com.FT.FinanceTracker.entity.Transaction.TransactionType.CREDIT)
            return "Income";

        return "Uncategorized";
    }
}
