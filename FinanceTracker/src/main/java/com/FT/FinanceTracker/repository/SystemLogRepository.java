package com.FT.FinanceTracker.repository;

import com.FT.FinanceTracker.entity.SystemLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SystemLogRepository extends JpaRepository<SystemLog, UUID> {

    List<SystemLog> findByLevel(String level);

    List<SystemLog> findBySource(String source);
}
