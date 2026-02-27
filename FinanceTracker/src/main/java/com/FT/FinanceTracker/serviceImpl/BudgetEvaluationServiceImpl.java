package com.FT.FinanceTracker.serviceImpl;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(BudgetEvaluationServiceImpl.class);

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

        for (Budget budget : budgets) {
            logger.info("EVALUATING BUDGET: {} (Limit: {})", budget.getCategory(), budget.getLimitAmount());

            BigDecimal spent = transactionRepository
                    .sumByUserCategoryAndCurrentPeriod(
                            user.getId(),
                            budget.getCategory(),
                            budget.getPeriodStart());
            
            logger.info("CALCULATED SPENT FOR {}: {}", budget.getCategory(), spent);

            if (spent != null && spent.compareTo(budget.getLimitAmount()) >= 0) {
                logger.warn("ALERT TRIGGERED FOR {}", budget.getCategory());
                // Check if an alert already exists for this category to avoid duplicates
                Alert alert = alertRepository.findByUser_IdAndCategory(user.getId(), budget.getCategory())
                        .orElse(new Alert());
                
                alert.setUser(user);
                alert.setCategory(budget.getCategory());
                alert.setCurrentSpent(spent);
                alert.setLimitAmount(budget.getLimitAmount());
                alert.setStatus(AlertStatus.ACTIVE);
                alert.setType("Budget Exceeded");
                alert.setSeverity("HIGH");
                alert.setMessage("Budget exceeded for " + budget.getCategory() + 
                    ": spent " + spent + " of " + budget.getLimitAmount() + " limit");

                alertRepository.save(alert);
            }
        }
    }
}
