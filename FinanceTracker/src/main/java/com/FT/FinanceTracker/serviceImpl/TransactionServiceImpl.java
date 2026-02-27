package com.FT.FinanceTracker.serviceImpl;

import java.util.UUID;
import org.springframework.stereotype.Service;
import com.FT.FinanceTracker.dto.TransactionOverrideDto;
import com.FT.FinanceTracker.entity.Transaction;
import com.FT.FinanceTracker.repository.TransactionRepository;
import com.FT.FinanceTracker.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
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

        transactionRepository.save(txn);
    }
}
