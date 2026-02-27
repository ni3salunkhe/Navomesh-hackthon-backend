package com.FT.FinanceTracker.repository;

import com.FT.FinanceTracker.entity.MerchantAlias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MerchantAliasRepository extends JpaRepository<MerchantAlias, Long> {

    @Query("SELECT m FROM MerchantAlias m ORDER BY m.priority DESC")
    List<MerchantAlias> findAllOrderedByPriority();

    List<MerchantAlias> findByNormalizedName(String normalizedName);
}
