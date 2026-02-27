package com.FT.FinanceTracker.service;

import com.FT.FinanceTracker.dto.BudgetRequestDto;

public interface BudgetService {
    String createBudget(BudgetRequestDto dto, String email);
}
