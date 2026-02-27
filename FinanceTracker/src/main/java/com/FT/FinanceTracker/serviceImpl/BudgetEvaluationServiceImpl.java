package com.FT.FinanceTracker.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.FT.FinanceTracker.entity.Alert;
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
        List<Budget> budgets = budgetRepository.findByUser(user);

        for (Budget budget : budgets) {
            double spent = transactionRepository.sumByUserAndCategoryAndPeriod(
                user,
                budget.getCategory(),
                budget.getPeriodStart(),
                budget.getPeriodEnd()
            );

            if (spent >= budget.getLimitAmount()) {
                Alert alert = new Alert();
                alert.setUser(user);
                alert.setCategory(budget.getCategory());
                alert.setCurrentSpent(spent);
                alert.setLimitAmount(budget.getLimitAmount());
                alert.setMessage(String.format("Budget exceeded for %s: spent %.2f of %.2f limit",
                        budget.getCategory(), spent, budget.getLimitAmount()));
                alert.setStatus(Alert.AlertStatus.ACTIVE);
                alertRepository.save(alert);
            }
        }
    }
}
