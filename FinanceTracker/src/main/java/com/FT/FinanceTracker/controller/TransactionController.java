package com.FT.FinanceTracker.controller;

import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.FT.FinanceTracker.dto.TransactionOverrideDto;
import com.FT.FinanceTracker.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final com.FT.FinanceTracker.repository.UserRepository userRepository;

    public TransactionController(TransactionService transactionService, 
                                 com.FT.FinanceTracker.repository.UserRepository userRepository) {
        this.transactionService = transactionService;
        this.userRepository = userRepository;
    }

    @PutMapping("/{id}/override")
    public ResponseEntity<?> overrideTransaction(
            @PathVariable UUID id,
            @RequestBody TransactionOverrideDto dto) {

        transactionService.overrideTransaction(id, dto);
        return ResponseEntity.ok("Transaction overridden successfully");
    }

    @GetMapping("/review")
    public ResponseEntity<?> getTransactionsForReview() {
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        com.FT.FinanceTracker.entity.User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(transactionService.getTransactionsForReview(user));
    }
}
