package com.FT.FinanceTracker.serviceImpl;

import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import com.FT.FinanceTracker.entity.RecurringPayment;
import com.FT.FinanceTracker.entity.Transaction;
import com.FT.FinanceTracker.entity.User;
import com.FT.FinanceTracker.repository.RecurringPaymentRepository;
import com.FT.FinanceTracker.repository.TransactionRepository;
import com.FT.FinanceTracker.service.RecurringDetectionService;

@Service
public class RecurringDetectionServiceImpl implements RecurringDetectionService {

    private final TransactionRepository transactionRepository;
    private final RecurringPaymentRepository recurringPaymentRepository;

    public RecurringDetectionServiceImpl(TransactionRepository transactionRepository,
                                         RecurringPaymentRepository recurringPaymentRepository) {
        this.transactionRepository = transactionRepository;
        this.recurringPaymentRepository = recurringPaymentRepository;
    }

    @Override
    public void detectRecurring(User user) {
        // Clear existing recurring payments for this user
        recurringPaymentRepository.deleteByUser(user);

        // Get distinct merchants for this user
        List<String> merchants = transactionRepository.findDistinctMerchantsByUser(user);

        for (String merchant : merchants) {
            List<Transaction> txns = transactionRepository
                    .findByUserAndNormalizedMerchantOrderByTransactionDateAsc(user, merchant);

            if (txns.size() >= 2) {
                // Calculate average interval between transactions
                long totalDays = 0;
                for (int i = 1; i < txns.size(); i++) {
                    totalDays += ChronoUnit.DAYS.between(
                            txns.get(i - 1).getTransactionDate(),
                            txns.get(i).getTransactionDate());
                }
                int avgInterval = (int) (totalDays / (txns.size() - 1));

                // If transactions occur roughly monthly (25-35 days) or weekly (5-9 days)
                if ((avgInterval >= 25 && avgInterval <= 35) || (avgInterval >= 5 && avgInterval <= 9)) {
                    double avgAmount = txns.stream()
                            .mapToDouble(Transaction::getAmount)
                            .average()
                            .orElse(0.0);

                    double confidence = Math.min(txns.size() * 0.2, 1.0);

                    RecurringPayment rp = new RecurringPayment();
                    rp.setUser(user);
                    rp.setMerchant(merchant);
                    rp.setAverageAmount(avgAmount);
                    rp.setIntervalDays(avgInterval);
                    rp.setConfidenceScore(confidence);

                    recurringPaymentRepository.save(rp);
                }
            }
        }
    }
}
