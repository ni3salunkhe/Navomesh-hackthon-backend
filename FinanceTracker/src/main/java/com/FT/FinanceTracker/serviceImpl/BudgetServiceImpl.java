package com.FT.FinanceTracker.serviceImpl;

import org.springframework.stereotype.Service;
import com.FT.FinanceTracker.dto.BudgetRequestDto;
import com.FT.FinanceTracker.dto.BudgetResponseDto;
import com.FT.FinanceTracker.entity.Budget;
import com.FT.FinanceTracker.entity.User;
import com.FT.FinanceTracker.repository.BudgetRepository;
import com.FT.FinanceTracker.repository.UserRepository;
import com.FT.FinanceTracker.repository.TransactionRepository;
import com.FT.FinanceTracker.service.BudgetService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final com.FT.FinanceTracker.service.SystemLogService logService;

    public BudgetServiceImpl(BudgetRepository budgetRepository,
                             UserRepository userRepository,
                             TransactionRepository transactionRepository,
                             com.FT.FinanceTracker.service.SystemLogService logService) {
        this.budgetRepository = budgetRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.logService = logService;
    }

    @Override
    public String createBudget(BudgetRequestDto dto, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Budget budget = new Budget();
        budget.setUser(user);
        budget.setCategory(dto.getCategory());
        budget.setLimitAmount(dto.getLimitAmount());
        budget.setPeriod(dto.getPeriod() != null ? dto.getPeriod() : Budget.BudgetPeriod.MONTHLY);
        budget.setStartDate(budget.getPeriodStart());

        budgetRepository.save(budget);

        logService.info("Budget created for user " + email + ": " + dto.getCategory() + " (" + dto.getLimitAmount() + ")", "BUDGET_SERVICE");

        return "Budget created successfully";
    }

    @Override
    public List<BudgetResponseDto> getUserBudgets(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Budget> budgets = budgetRepository.findByUser(user);

        return budgets.stream().map(budget -> {
            BudgetResponseDto dto = new BudgetResponseDto();
            dto.setId(budget.getId());
            dto.setCategory(budget.getCategory());
            dto.setLimitAmount(budget.getLimitAmount());
            dto.setPeriod(budget.getPeriod());

            BigDecimal spent = transactionRepository.sumByUserCategoryAndCurrentPeriod(
                    user.getId(),
                    budget.getCategory(),
                    budget.getPeriodStart()
            );

            dto.setCurrentSpent(spent != null ? spent : BigDecimal.ZERO);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteBudget(UUID budgetId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        if (!budget.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to delete this budget");
        }

        budgetRepository.delete(budget);
        
        logService.info("Budget " + budgetId + " deleted for user " + email, "BUDGET_SERVICE");
    }
}
