package com.FT.FinanceTracker.service;

import org.springframework.web.multipart.MultipartFile;

public interface ParsingService {
	String extractText(MultipartFile file);
}
