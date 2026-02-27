package com.FT.FinanceTracker.repository;

import com.FT.FinanceTracker.entity.Budget;
import com.FT.FinanceTracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, UUID> {

    List<Budget> findByUser(User user);

    List<Budget> findByUser_Id(UUID userId);

    List<Budget> findByUserAndCategory(User user, String category);
}
