package com.FT.FinanceTracker.serviceImpl;

import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.FT.FinanceTracker.entity.RecurringPayment;
import com.FT.FinanceTracker.entity.Transaction;
import com.FT.FinanceTracker.entity.User;
import com.FT.FinanceTracker.repository.RecurringPaymentRepository;
import com.FT.FinanceTracker.repository.TransactionRepository;
import com.FT.FinanceTracker.service.RecurringDetectionService;

import jakarta.transaction.Transactional;

@Service
public class RecurringDetectionServiceImpl implements RecurringDetectionService {

    private final TransactionRepository transactionRepository;
    private final RecurringPaymentRepository recurringPaymentRepository;

    public RecurringDetectionServiceImpl(TransactionRepository transactionRepository,
                                         RecurringPaymentRepository recurringPaymentRepository) {
        this.transactionRepository = transactionRepository;
        this.recurringPaymentRepository = recurringPaymentRepository;
    }

    @Transactional
    public void detectRecurring(User user) {

        List<Transaction> transactions =
            transactionRepository.findDebitByUser(user.getId());

        Map<String, List<Transaction>> grouped =
            transactions.stream()
                .filter(t -> t.getNormalizedMerchant() != null)
                .collect(Collectors.groupingBy(Transaction::getNormalizedMerchant));

        for (String merchant : grouped.keySet()) {

            List<Transaction> list = grouped.get(merchant);

            if (list.size() < 3) continue;

            list.sort(Comparator.comparing(Transaction::getTransactionDate));

            if (isMonthlyRecurring(list)) {

                saveRecurring(user, merchant, list);
            }
        }
    }
    private boolean isMonthlyRecurring(List<Transaction> list) {

        for (int i = 1; i < list.size(); i++) {

            long days =
                ChronoUnit.DAYS.between(
                    list.get(i - 1).getTransactionDate(),
                    list.get(i).getTransactionDate()
                );

            if (days < 25 || days > 35)
                return false;

            if (!isAmountSimilar(
                    list.get(i - 1).getAmount(),
                    list.get(i).getAmount()))
                return false;
        }

        return true;
    }
    private boolean isAmountSimilar(Double a, Double b) {

        double tolerance = a * 0.05;

        return Math.abs(a - b) <= tolerance;
    }

    private void saveRecurring(User user, String merchant, List<Transaction> list) {
        if (recurringPaymentRepository.existsByUserAndMerchant(user, merchant)) {
            return;
        }

        double sumAmount = list.stream()
                .mapToDouble(t -> t.getAmount() == null ? 0.0 : t.getAmount())
                .sum();
        double avgAmount = sumAmount / list.size();

        long totalDays = ChronoUnit.DAYS.between(
                list.get(0).getTransactionDate(),
                list.get(list.size() - 1).getTransactionDate()
        );
        int avgInterval = (int) (totalDays / (list.size() - 1));

        double confidence = Math.min(0.95, 0.7 + (list.size() * 0.05));

        RecurringPayment rp = new RecurringPayment();
        rp.setUser(user);
        rp.setMerchant(merchant);
        rp.setAverageAmount(avgAmount);
        rp.setIntervalDays(avgInterval);
        rp.setConfidenceScore(confidence);
        
        recurringPaymentRepository.save(rp);

        for (Transaction t : list) {
            t.setRecurringFlag(true);
        }
        transactionRepository.saveAll(list);
    }
}
