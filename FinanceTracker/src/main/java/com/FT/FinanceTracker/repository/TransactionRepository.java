package com.FT.FinanceTracker.repository;

import com.FT.FinanceTracker.entity.Transaction;
import com.FT.FinanceTracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findByUser(User user);

    List<Transaction> findByUserOrderByTransactionDateDesc(User user);

    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId AND t.type = com.FT.FinanceTracker.entity.Transaction.TransactionType.DEBIT")
    List<Transaction> findDebitByUser(@Param("userId") UUID userId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
           "WHERE t.user = :user AND t.systemCategory = :category " +
           "AND t.transactionDate BETWEEN :startDate AND :endDate " +
           "AND t.type = com.FT.FinanceTracker.entity.Transaction.TransactionType.DEBIT")
    BigDecimal sumByUserAndCategoryAndPeriod(
            @Param("user") User user,
            @Param("category") String category,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    List<Transaction> findByUserAndNormalizedMerchantOrderByTransactionDateAsc(User user, String normalizedMerchant);

    @Query("SELECT DISTINCT t.normalizedMerchant FROM Transaction t WHERE t.user = :user AND t.normalizedMerchant IS NOT NULL")
    List<String> findDistinctMerchantsByUser(@Param("user") User user);

    @Query("SELECT t FROM Transaction t WHERE t.user = :user AND t.type = com.FT.FinanceTracker.entity.Transaction.TransactionType.DEBIT AND t.normalizedMerchant != 'UNKNOWN' ORDER BY t.normalizedMerchant, t.transactionDate ASC")
    List<Transaction> findRecurringCandidatesByUser(@Param("user") User user);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
           "WHERE t.user = :user AND t.type = com.FT.FinanceTracker.entity.Transaction.TransactionType.DEBIT")
    BigDecimal sumTotalSpentByUser(@Param("user") User user);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
           "WHERE t.user = :user AND t.type = com.FT.FinanceTracker.entity.Transaction.TransactionType.CREDIT")
    BigDecimal sumTotalIncomeByUser(@Param("user") User user);

    @Query("""
    SELECT COALESCE(SUM(t.amount), 0)
    FROM Transaction t
    WHERE t.user.id = :userId
    AND t.systemCategory = :category
    AND t.type = com.FT.FinanceTracker.entity.Transaction.TransactionType.DEBIT
    AND t.transactionDate >= :startDate
    """)
    BigDecimal sumByUserCategoryAndCurrentPeriod(
            @Param("userId") UUID userId, 
            @Param("category") String category,
            @Param("startDate") LocalDate startDate);

    long countByUser(User user);

    List<Transaction> findByUserAndConfidenceScoreLessThanAndUserOverrideCategoryIsNullOrderByTransactionDateDesc(User user, Double confidenceThreshold);
}
