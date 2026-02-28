package com.FT.FinanceTracker.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.FT.FinanceTracker.dto.SmsWebhookRequest;
import com.FT.FinanceTracker.entity.Transaction;
import com.FT.FinanceTracker.entity.User;
import com.FT.FinanceTracker.repository.TransactionRepository;
import com.FT.FinanceTracker.repository.UserRepository;
import com.FT.FinanceTracker.service.SystemLogService;
import com.FT.FinanceTracker.service.UpiSmsService;

@Service
public class UpiSmsServiceImpl implements UpiSmsService {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UpiSmsServiceImpl.class);

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final SystemLogService logService;

    private static final Pattern AMOUNT_PATTERN = Pattern.compile("(?:Rs\\.?|INR)\\s?([0-9,]+(?:\\.\\d{1,2})?)", Pattern.CASE_INSENSITIVE);
    private static final Pattern REF_PATTERN = Pattern.compile("Ref\\s?No(?:\\.|:)?\\s?(\\d+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern MERCHANT_PATTERN = Pattern.compile("to\\s([A-Za-z0-9@._\\-\\s]+?)(?:\\.|,|\\s[A-Z]|$)", Pattern.CASE_INSENSITIVE);

    public UpiSmsServiceImpl(TransactionRepository transactionRepository, UserRepository userRepository, SystemLogService logService) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.logService = logService;
    }

    @Override
    public void processUpiSms(SmsWebhookRequest request) {
        String message = request.getMessage();
        if (message == null || message.isEmpty()) return;

        logger.info("Processing SMS Webhook from: {}", request.getFrom());

        if (isUpiTransaction(message)) {
            User user = null;
            if (request.getUserEmail() != null && !request.getUserEmail().isEmpty()) {
                user = userRepository.findByEmail(request.getUserEmail()).orElse(null);
            }

            if (user == null) {
                logger.warn("Received SMS for unknown or missing user: {}. Falling back to first available user for hackathon demo.", request.getUserEmail());
                user = userRepository.findAll().stream().findFirst().orElse(null);
                if (user == null) {
                    logger.error("No users found in database to attach SMS transaction.");
                    return;
                }
            }

            Transaction tx = parseUpiSms(message, user);
            if (tx.getAmount() != null && tx.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                transactionRepository.save(tx);
                logService.info("Real-time UPI SMS transaction processed for amount: " + tx.getAmount(), "UPI_WEBHOOK");
                logger.info("Saved UPI transaction from SMS: {}", tx.getRawDescription());
            } else {
                 logger.warn("Failed to extract valid amount from SMS: {}", message);
            }
        } else {
            logger.info("SMS is not recognized as a UPI transaction.");
        }
    }

    private boolean isUpiTransaction(String message) {
        String lowerMsg = message.toLowerCase();
        return lowerMsg.contains("upi") || lowerMsg.contains("debited") || lowerMsg.contains("credited") || lowerMsg.contains("ref no");
    }

    private Transaction parseUpiSms(String sms, User user) {
        Matcher amountMatcher = AMOUNT_PATTERN.matcher(sms);
        Matcher refMatcher = REF_PATTERN.matcher(sms);
        Matcher merchantMatcher = MERCHANT_PATTERN.matcher(sms);

        BigDecimal amount = BigDecimal.ZERO;
        String refNo = "";
        String merchant = "Unknown UPI Transfer";

        if (amountMatcher.find()) {
            amount = new BigDecimal(amountMatcher.group(1).replace(",", ""));
        }

        if (refMatcher.find()) {
            refNo = refMatcher.group(1);
        }

        if (merchantMatcher.find()) {
            merchant = merchantMatcher.group(1).trim();
            if (merchant.toLowerCase().startsWith("vpa ")) {
                merchant = merchant.substring(4);
            }
        }
        
        if (!refNo.isEmpty()) {
            merchant = merchant + " (Ref: " + refNo + ")";
        }

        boolean isCredit = sms.toLowerCase().contains("credited") || sms.toLowerCase().contains("deposited");
        
        Transaction tx = new Transaction();
        tx.setUser(user);
        tx.setAmount(amount);
        tx.setType(isCredit ? Transaction.TransactionType.CREDIT : Transaction.TransactionType.DEBIT);
        tx.setTransactionDate(LocalDate.now()); 
        tx.setRawDescription(sms);
        
        tx.setNormalizedMerchant(merchant);
        tx.setSystemCategory("UPI Transfer");
        tx.setConfidenceScore(0.95);
        tx.setModelVersion("SMS_REGEX_v1");
        
        return tx;
    }
}
