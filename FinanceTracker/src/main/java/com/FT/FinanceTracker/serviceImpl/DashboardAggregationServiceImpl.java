package com.FT.FinanceTracker.serviceImpl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.FT.FinanceTracker.dto.DashboardResponseDto;
import com.FT.FinanceTracker.dto.RecurringDto;
import com.FT.FinanceTracker.dto.TransactionDto;
import com.FT.FinanceTracker.entity.RecurringPayment;
import com.FT.FinanceTracker.entity.Transaction;
import com.FT.FinanceTracker.entity.Transaction.TransactionType;
import com.FT.FinanceTracker.entity.User;
import com.FT.FinanceTracker.repository.RecurringPaymentRepository;
import com.FT.FinanceTracker.repository.TransactionRepository;
import com.FT.FinanceTracker.service.DashboardAggregationService;
import com.FT.FinanceTracker.utils.TransactionMapper;

@Service
public class DashboardAggregationServiceImpl implements DashboardAggregationService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final RecurringPaymentRepository recurringPaymentRepository;

    public DashboardAggregationServiceImpl(TransactionRepository transactionRepository,
                                           TransactionMapper transactionMapper,
                                           RecurringPaymentRepository recurringPaymentRepository) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.recurringPaymentRepository = recurringPaymentRepository;
    }

    @Override
    public DashboardResponseDto buildDashboard(User user) {
        DashboardResponseDto dashboard = new DashboardResponseDto();

        List<Transaction> allTransactions = transactionRepository.findByUser(user);
        
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;
        Map<String, BigDecimal> categoryBreakdownMap = new HashMap<>();

        for (Transaction t : allTransactions) {
            BigDecimal amount = t.getAmount() != null ? t.getAmount() : BigDecimal.ZERO;
            
            if (t.getType() == TransactionType.CREDIT) {
                totalIncome = totalIncome.add(amount);
            } else {
                totalExpense = totalExpense.add(amount);
                String cat = t.getSystemCategory() != null ? t.getSystemCategory() : "Uncategorized";
                categoryBreakdownMap.merge(cat, amount, BigDecimal::add);
            }
        }

        dashboard.setTotalIncome(totalIncome.doubleValue());
        dashboard.setTotalExpense(totalExpense.doubleValue());
        dashboard.setNetBalance(totalIncome.subtract(totalExpense).doubleValue());
        dashboard.setTransactionCount(transactionRepository.countByUser(user));

        // Category breakdown conversion
        Map<String, Double> finalBreakdown = categoryBreakdownMap.entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> e.getValue().doubleValue()
                ));
        dashboard.setCategoryBreakdown(finalBreakdown);

        // Recent transactions (last 10)
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

        return dashboard;
    }
}
