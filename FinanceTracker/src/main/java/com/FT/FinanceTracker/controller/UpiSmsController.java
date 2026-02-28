package com.FT.FinanceTracker.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FT.FinanceTracker.dto.SmsWebhookRequest;
import com.FT.FinanceTracker.service.UpiSmsService;

@RestController
@RequestMapping("/api/upi")
public class UpiSmsController {

    private final UpiSmsService upiSmsService;

    @Value("${upi.webhook.secret:navomesh-hackathon-secret}")
    private String webhookSecret;

    public UpiSmsController(UpiSmsService upiSmsService) {
        this.upiSmsService = upiSmsService;
    }

    @PostMapping("/sms")
    public ResponseEntity<?> receiveSms(
            @RequestHeader(value = "X-API-KEY", required = false) String apiKey,
            @RequestBody SmsWebhookRequest request) {

        if (apiKey == null || !apiKey.equals(webhookSecret)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid or missing API Key");
        }

        upiSmsService.processUpiSms(request);

        return ResponseEntity.ok("SMS Received and Processed Successfully");
    }
}
