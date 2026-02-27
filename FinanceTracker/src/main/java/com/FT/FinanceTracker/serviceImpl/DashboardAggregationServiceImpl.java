package com.FT.FinanceTracker.serviceImpl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.FT.FinanceTracker.dto.AlertDto;
import com.FT.FinanceTracker.dto.DashboardResponseDto;
import com.FT.FinanceTracker.dto.RecurringDto;
import com.FT.FinanceTracker.dto.TransactionDto;
import com.FT.FinanceTracker.entity.Alert;
import com.FT.FinanceTracker.entity.RecurringPayment;
import com.FT.FinanceTracker.entity.Transaction;
import com.FT.FinanceTracker.entity.User;
import com.FT.FinanceTracker.repository.AlertRepository;
import com.FT.FinanceTracker.repository.RecurringPaymentRepository;
import com.FT.FinanceTracker.repository.TransactionRepository;
import com.FT.FinanceTracker.service.DashboardAggregationService;
import com.FT.FinanceTracker.utils.TransactionMapper;

@Service
public class DashboardAggregationServiceImpl implements DashboardAggregationService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final RecurringPaymentRepository recurringPaymentRepository;
    private final AlertRepository alertRepository;

    public DashboardAggregationServiceImpl(TransactionRepository transactionRepository,
                                           TransactionMapper transactionMapper,
                                           RecurringPaymentRepository recurringPaymentRepository,
                                           AlertRepository alertRepository) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.recurringPaymentRepository = recurringPaymentRepository;
        this.alertRepository = alertRepository;
    }

    @Override
    public DashboardResponseDto buildDashboard(User user) {
        DashboardResponseDto dashboard = new DashboardResponseDto();

        BigDecimal totalIncome = transactionRepository.sumTotalIncomeByUser(user);
        BigDecimal totalExpense = transactionRepository.sumTotalSpentByUser(user);

        if (totalIncome == null) totalIncome = BigDecimal.ZERO;
        if (totalExpense == null) totalExpense = BigDecimal.ZERO;

        dashboard.setTotalIncome(totalIncome);
        dashboard.setTotalExpense(totalExpense);
        dashboard.setNetBalance(totalIncome.subtract(totalExpense));
        dashboard.setTransactionCount(transactionRepository.countByUser(user));

        // Category breakdown reflecting overrides
        List<Transaction> transactions = transactionRepository.findByUser(user);
        Map<String, BigDecimal> categoryBreakdown = new HashMap<>();
        for (Transaction t : transactions) {
            String cat = t.getUserOverrideCategory() != null ? t.getUserOverrideCategory() : 
                         (t.getSystemCategory() != null ? t.getSystemCategory() : "Uncategorized");
            categoryBreakdown.merge(cat, t.getAmount(), BigDecimal::add);
        }
        dashboard.setCategoryBreakdown(categoryBreakdown);

        // Recent transactions reflecting overrides
        List<Transaction> recent = transactionRepository.findByUserOrderByTransactionDateDesc(user);
        List<TransactionDto> recentDtos = recent.stream()
                .limit(10)
                .map(transactionMapper::toDto)
                .collect(Collectors.toList());
        dashboard.setRecentTransactions(recentDtos);

        // Recurring Payments
        List<RecurringPayment> recurring = recurringPaymentRepository.findByUser(user);
        List<RecurringDto> recurringDtos = recurring.stream()
                .map(r -> new RecurringDto(
                        r.getMerchant(),
                        r.getAverageAmount(),
                        r.getIntervalDays(),
                        r.getConfidenceScore()))
                .collect(Collectors.toList());
        dashboard.setRecurringPayments(recurringDtos);

        // Alerts
        List<Alert> alerts = alertRepository.findByUser_Id(user.getId());
        List<AlertDto> alertDtos = alerts.stream()
                .map(a -> new AlertDto(
                        a.getCategory(),
                        a.getCurrentSpent(),
                        a.getLimitAmount(),
                        a.getStatus().name(),
                        a.getMessage(),
                        a.getType(),
                        a.getSeverity()))
                .collect(Collectors.toList());
        dashboard.setAlerts(alertDtos);

        // Review Count (confidence < 0.8 and not yet overridden)
        List<Transaction> reviewItems = transactionRepository.findByUserAndConfidenceScoreLessThanAndUserOverrideCategoryIsNullOrderByTransactionDateDesc(user, 0.8);
        dashboard.setReviewCount(reviewItems.size());

        return dashboard;
    }
}
