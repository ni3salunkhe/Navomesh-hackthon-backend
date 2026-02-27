package com.FT.FinanceTracker.serviceImpl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.FT.FinanceTracker.entity.Alert;
import com.FT.FinanceTracker.entity.Alert.AlertStatus;
import com.FT.FinanceTracker.entity.Budget;
import com.FT.FinanceTracker.entity.User;
import com.FT.FinanceTracker.repository.AlertRepository;
import com.FT.FinanceTracker.repository.BudgetRepository;
import com.FT.FinanceTracker.repository.TransactionRepository;
import com.FT.FinanceTracker.service.BudgetEvaluationService;

@Service
public class BudgetEvaluationServiceImpl implements BudgetEvaluationService {

    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;
    private final AlertRepository alertRepository;

    public BudgetEvaluationServiceImpl(BudgetRepository budgetRepository,
                                       TransactionRepository transactionRepository,
                                       AlertRepository alertRepository) {
        this.budgetRepository = budgetRepository;
        this.transactionRepository = transactionRepository;
        this.alertRepository = alertRepository;
    }

    @Override
    public void evaluate(User user) {

        List<Budget> budgets = budgetRepository.findByUser_Id(user.getId());

        java.time.LocalDate startDate = java.time.LocalDate.now().minusDays(30);

        for (Budget budget : budgets) {

            BigDecimal spent = transactionRepository
                    .sumByUserCategoryAndCurrentPeriod(
                            user.getId(),
                            budget.getCategory(),
                            startDate);

            if (spent != null && spent.compareTo(budget.getLimitAmount()) >= 0) {

                // Check if an alert already exists for this category to avoid duplicates
                boolean exists = alertRepository
                        .findByUser_Id(user.getId())
                        .stream()
                        .anyMatch(a -> a.getCategory().equals(budget.getCategory()) && a.getStatus() == AlertStatus.ACTIVE);

                if (!exists) {
                    Alert alert = new Alert();
                    alert.setUser(user);
                    alert.setCategory(budget.getCategory());
                    alert.setCurrentSpent(spent);
                    alert.setLimitAmount(budget.getLimitAmount());
                    alert.setStatus(AlertStatus.ACTIVE);
                    alert.setMessage("Budget exceeded for " + budget.getCategory() + 
                        ": spent " + spent + " of " + budget.getLimitAmount() + " limit");

                    alertRepository.save(alert);
                }
            }
        }
    }
}
