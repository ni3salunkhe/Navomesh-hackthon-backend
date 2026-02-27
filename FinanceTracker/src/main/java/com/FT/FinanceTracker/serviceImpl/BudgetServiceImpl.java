package com.FT.FinanceTracker.serviceImpl;

import org.springframework.stereotype.Service;
import com.FT.FinanceTracker.dto.BudgetRequestDto;
import com.FT.FinanceTracker.entity.Budget;
import com.FT.FinanceTracker.entity.User;
import com.FT.FinanceTracker.repository.BudgetRepository;
import com.FT.FinanceTracker.repository.UserRepository;
import com.FT.FinanceTracker.service.BudgetService;

@Service
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;

    public BudgetServiceImpl(BudgetRepository budgetRepository,
                             UserRepository userRepository) {
        this.budgetRepository = budgetRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String createBudget(BudgetRequestDto dto, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Budget budget = new Budget();
        budget.setUser(user);
        budget.setCategory(dto.getCategory());
        budget.setLimitAmount(dto.getLimitAmount());
        budget.setPeriod(dto.getPeriod());

        budgetRepository.save(budget);

        return "Budget created successfully";
    }
}
