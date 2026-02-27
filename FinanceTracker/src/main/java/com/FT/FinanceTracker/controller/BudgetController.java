package com.FT.FinanceTracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.FT.FinanceTracker.dto.BudgetRequestDto;
import com.FT.FinanceTracker.service.BudgetService;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public ResponseEntity<?> createBudget(@RequestBody BudgetRequestDto dto) {
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(budgetService.createBudget(dto, email));
    }

    @GetMapping
    public ResponseEntity<?> getUserBudgets() {
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(budgetService.getUserBudgets(email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBudget(@PathVariable java.util.UUID id) {
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        budgetService.deleteBudget(id, email);
        return ResponseEntity.ok().build();
    }
}
