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
    public ResponseEntity<?> createBudget(@RequestBody BudgetRequestDto dto,
                                          @RequestParam String email) {

        return ResponseEntity.ok(budgetService.createBudget(dto, email));
    }
}
