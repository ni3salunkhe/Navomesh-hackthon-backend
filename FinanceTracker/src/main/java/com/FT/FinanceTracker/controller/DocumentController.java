package com.FT.FinanceTracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.FT.FinanceTracker.dto.DashboardResponseDto;
import com.FT.FinanceTracker.service.DocumentProcessingService;

@RestController
@RequestMapping("/api")
public class DocumentController {

    private final DocumentProcessingService documentProcessingService;

    public DocumentController(DocumentProcessingService documentProcessingService) {
        this.documentProcessingService = documentProcessingService;
    }

    @PostMapping("/upload")
    public ResponseEntity<DashboardResponseDto> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("email") String userEmail) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals("application/pdf")) {
            return ResponseEntity.badRequest().build();
        }

        DashboardResponseDto response = documentProcessingService.processDocument(file, userEmail);
        return ResponseEntity.ok(response);
    }
}
