package com.FT.FinanceTracker.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.FT.FinanceTracker.dto.DashboardResponseDto;
import com.FT.FinanceTracker.dto.TransactionDto;
import com.FT.FinanceTracker.entity.Transaction;
import com.FT.FinanceTracker.entity.User;
import com.FT.FinanceTracker.repository.TransactionRepository;
import com.FT.FinanceTracker.service.DashboardAggregationService;
import com.FT.FinanceTracker.utils.TransactionMapper;

@Service
public class DashboardAggregationServiceImpl implements DashboardAggregationService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public DashboardAggregationServiceImpl(TransactionRepository transactionRepository,
                                            TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public DashboardResponseDto buildDashboard(User user) {
        DashboardResponseDto dashboard = new DashboardResponseDto();

        double totalIncome = transactionRepository.sumTotalIncomeByUser(user);
        double totalExpense = transactionRepository.sumTotalSpentByUser(user);

        dashboard.setTotalIncome(totalIncome);
        dashboard.setTotalExpense(totalExpense);
        dashboard.setNetBalance(totalIncome - totalExpense);
        dashboard.setTransactionCount(transactionRepository.countByUser(user));

        // Category breakdown
        List<Transaction> transactions = transactionRepository.findByUser(user);
        Map<String, Double> categoryBreakdown = new HashMap<>();
        for (Transaction t : transactions) {
            String cat = t.getSystemCategory() != null ? t.getSystemCategory() : "Uncategorized";
            categoryBreakdown.merge(cat, t.getAmount(), Double::sum);
        }
        dashboard.setCategoryBreakdown(categoryBreakdown);

        // Recent transactions (last 10)
        List<Transaction> recent = transactionRepository.findByUserOrderByTransactionDateDesc(user);
        List<TransactionDto> recentDtos = recent.stream()
                .limit(10)
                .map(transactionMapper::toDto)
                .collect(Collectors.toList());
        dashboard.setRecentTransactions(recentDtos);

        return dashboard;
    }
}
