package com.FT.FinanceTracker.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.FT.FinanceTracker.dto.DashboardResponseDto;
import com.FT.FinanceTracker.entity.Transaction;
import com.FT.FinanceTracker.entity.Transaction.TransactionType;
import com.FT.FinanceTracker.entity.User;
import com.FT.FinanceTracker.repository.TransactionRepository;
import com.FT.FinanceTracker.repository.UserRepository;
import com.FT.FinanceTracker.service.DashboardAggregationService;
import com.FT.FinanceTracker.service.RecurringDetectionService;

@RestController
@RequestMapping("/api/test-recurring")
public class TestRecurringController {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final RecurringDetectionService recurringDetectionService;
    private final DashboardAggregationService dashboardAggregationService;

    public TestRecurringController(UserRepository userRepository,
                                   TransactionRepository transactionRepository,
                                   RecurringDetectionService recurringDetectionService,
                                   DashboardAggregationService dashboardAggregationService) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.recurringDetectionService = recurringDetectionService;
        this.dashboardAggregationService = dashboardAggregationService;
    }

    @GetMapping
    public ResponseEntity<?> testRecurring() {
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        // Add 3 monthly transactions for Netflix -> should detect
        createTxn(user, "Netflix", 15.99, LocalDate.of(2026, 1, 15));
        createTxn(user, "Netflix", 15.99, LocalDate.of(2026, 2, 15));
        createTxn(user, "Netflix", 15.99, LocalDate.of(2026, 3, 15));

        // Add 2 manual rent payments -> should not detect (needs 3)
        createTxn(user, "Rent", 1000.0, LocalDate.of(2026, 2, 1));
        createTxn(user, "Rent", 1000.0, LocalDate.of(2026, 3, 1));

        // Add random grocery dates -> should not detect (uneven gaps, different amounts)
        createTxn(user, "Walmart", 50.0, LocalDate.of(2026, 2, 5));
        createTxn(user, "Walmart", 100.0, LocalDate.of(2026, 2, 18));
        createTxn(user, "Walmart", 200.0, LocalDate.of(2026, 3, 10));

        // Run detection
        recurringDetectionService.detectRecurring(user);

        // Fetch dashboard
        DashboardResponseDto dashboard = dashboardAggregationService.buildDashboard(user);
        return ResponseEntity.ok(dashboard);
    }

    private void createTxn(User user, String merchant, double amount, LocalDate date) {
        Transaction t = new Transaction();
        t.setUser(user);
        t.setNormalizedMerchant(merchant);
        t.setAmount(BigDecimal.valueOf(amount));
        t.setTransactionDate(date);
        t.setType(TransactionType.DEBIT);
        t.setSystemCategory("Test");
        t.setRecurringFlag(false);
        transactionRepository.save(t);
    }
}
