package com.FT.FinanceTracker.repository;

import com.FT.FinanceTracker.entity.Alert;
import com.FT.FinanceTracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AlertRepository extends JpaRepository<Alert, UUID> {

    List<Alert> findByUser(User user);

    List<Alert> findByUserAndStatus(User user, Alert.AlertStatus status);

    long countByUserAndStatus(User user, Alert.AlertStatus status);
}
