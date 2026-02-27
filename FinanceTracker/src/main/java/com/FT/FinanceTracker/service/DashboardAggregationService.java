package com.FT.FinanceTracker.service;

import com.FT.FinanceTracker.dto.DashboardResponseDto;
import com.FT.FinanceTracker.entity.User;

public interface DashboardAggregationService {
	DashboardResponseDto buildDashboard(User user);
}
