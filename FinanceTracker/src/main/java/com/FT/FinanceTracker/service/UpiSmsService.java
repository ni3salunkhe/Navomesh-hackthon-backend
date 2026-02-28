package com.FT.FinanceTracker.service;

import com.FT.FinanceTracker.dto.SmsWebhookRequest;

public interface UpiSmsService {
    void processUpiSms(SmsWebhookRequest request);
}
