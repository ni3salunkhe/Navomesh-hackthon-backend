package com.FT.FinanceTracker.repository;

import com.FT.FinanceTracker.entity.RecurringPayment;
import com.FT.FinanceTracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RecurringPaymentRepository extends JpaRepository<RecurringPayment, UUID> {

    List<RecurringPayment> findByUser(User user);

    void deleteByUser(User user);
}
