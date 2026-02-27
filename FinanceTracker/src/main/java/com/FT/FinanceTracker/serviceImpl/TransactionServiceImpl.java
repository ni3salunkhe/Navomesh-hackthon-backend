package com.FT.FinanceTracker.serviceImpl;

import java.util.UUID;
import java.util.List;
import org.springframework.stereotype.Service;
import com.FT.FinanceTracker.dto.TransactionOverrideDto;
import com.FT.FinanceTracker.entity.Transaction;
import com.FT.FinanceTracker.entity.User;
import com.FT.FinanceTracker.repository.TransactionRepository;
import com.FT.FinanceTracker.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(TransactionServiceImpl.class);
    private final TransactionRepository transactionRepository;
    private final com.FT.FinanceTracker.service.SystemLogService logService;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                   com.FT.FinanceTracker.service.SystemLogService logService) {
        this.transactionRepository = transactionRepository;
        this.logService = logService;
    }

    @Override
    public void overrideTransaction(UUID transactionId,
                                    TransactionOverrideDto dto) {

        Transaction txn = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (dto.getCategory() != null) {
            txn.setUserOverrideCategory(dto.getCategory());
        }

        if (dto.getMerchant() != null) {
            txn.setUserOverrideMerchant(dto.getMerchant());
        }

        if (dto.getAmount() != null) {
            txn.setAmount(dto.getAmount());
        }

        transactionRepository.save(txn);
        
        logService.info("Transaction " + transactionId + " overridden by user. Changes: category=" + dto.getCategory() + ", merchant=" + dto.getMerchant() + ", amount=" + dto.getAmount(), "TRANSACTION_SERVICE");
    }

    @Override
    public List<Transaction> getTransactionsForReview(User user) {
        // Human review required for confidence < 0.8 (80%)
        List<Transaction> items = transactionRepository.findByUserAndConfidenceScoreLessThanAndUserOverrideCategoryIsNullOrderByTransactionDateDesc(user, 0.8);
        logger.info("Found {} transactions for review for user {}", items.size(), user.getEmail());
        return items;
    }
}
