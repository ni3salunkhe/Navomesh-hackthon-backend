package com.FT.FinanceTracker.service;

import org.springframework.web.multipart.MultipartFile;

import com.FT.FinanceTracker.dto.DashboardResponseDto;

public interface DocumentProcessingService {
	DashboardResponseDto processDocument(MultipartFile file, String userEmail);
}
