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

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PutMapping("/{id}/override")
    public ResponseEntity<?> overrideTransaction(
            @PathVariable UUID id,
            @RequestBody TransactionOverrideDto dto) {

        transactionService.overrideTransaction(id, dto);
        return ResponseEntity.ok("Transaction overridden successfully");
    }
}
