package com.FT.FinanceTracker.serviceImpl;

import org.springframework.stereotype.Service;

import com.FT.FinanceTracker.dto.MerchantInfo;
import com.FT.FinanceTracker.dto.TransactionDto;
import com.FT.FinanceTracker.service.ConfidanceService;

@Service
public class ConfidanceServiceImpl implements ConfidanceService {

    @Override
    public double calculate(TransactionDto dto, MerchantInfo merchantInfo, String category) {
        double score = 0.0;

        score += merchantInfo.getConfidenceScore();

        if (!category.equals("Uncategorized"))
            score += 0.3;

        if (dto.getType() != null)
            score += 0.2;

        return Math.min(score, 1.0);
    }
}
