package com.FT.FinanceTracker.service;

import com.FT.FinanceTracker.dto.BudgetRequestDto;

import com.FT.FinanceTracker.dto.BudgetResponseDto;
import java.util.List;
import java.util.UUID;

public interface BudgetService {
    String createBudget(BudgetRequestDto dto, String email);
    List<BudgetResponseDto> getUserBudgets(String email);
    void deleteBudget(UUID budgetId, String email);
}
