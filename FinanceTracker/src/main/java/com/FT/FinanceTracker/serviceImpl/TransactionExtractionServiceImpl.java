package com.FT.FinanceTracker.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.FT.FinanceTracker.dto.TransactionDto;
import com.FT.FinanceTracker.entity.Transaction.TransactionType;
import com.FT.FinanceTracker.service.TransactionExtractionService;

@Service
public class TransactionExtractionServiceImpl implements TransactionExtractionService {

    private static final Pattern DATE_PATTERN = Pattern.compile("(\\d{2}[-/]\\d{2}[-/]\\d{2,4})");
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("([0-9,]+\\.\\d{2})");

    private static final DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter dtf3 = DateTimeFormatter.ofPattern("dd-MMM-yyyy", java.util.Locale.ENGLISH);

    @Override
    public List<TransactionDto> extractTransactions(String rawText) {
        if (rawText == null || rawText.isEmpty()) return new ArrayList<>();

        List<TransactionDto> list = new ArrayList<>();
        
        // Strategy: Split by Date since most transactions start with a date
        String[] blocks = rawText.split("(?=\\d{2}-\\d{2}-\\d{4}|\\d{2}/\\d{2}/\\d{4})");
        
        for (String block : blocks) {
            block = block.trim();
            if (block.isEmpty() || !startsWithDate(block)) continue;

            try {
                processBlock(block, list);
            } catch (Exception e) {
                // Skip problematic blocks but continue
                System.err.println("Failed to process block: " + block.substring(0, Math.min(block.length(), 50)));
            }
        }

        return list;
    }

    private boolean startsWithDate(String block) {
        return DATE_PATTERN.matcher(block).lookingAt();
    }

    private void processBlock(String block, List<TransactionDto> list) {
        // Extract Date
        Matcher dateMatcher = DATE_PATTERN.matcher(block);
        if (!dateMatcher.find()) return;
        String dateStr = dateMatcher.group(1);
        
        // Remaining text for description and amount
        String remaining = block.substring(dateMatcher.end()).trim();
        
        // Find all amounts (Withdrawal/Deposit and Balance)
        Matcher amountMatcher = AMOUNT_PATTERN.matcher(remaining);
        List<String> amounts = new ArrayList<>();
        int lastAmountEnd = -1;
        while (amountMatcher.find()) {
            amounts.add(amountMatcher.group(1));
            lastAmountEnd = amountMatcher.end();
        }

        if (amounts.isEmpty()) return;

        TransactionDto dto = new TransactionDto();
        dto.setDate(parseDate(dateStr));

        // The last amount is usually the balance. 
        // The one before it is either Withdrawal or Deposit.
        BigDecimal amount = BigDecimal.ZERO;
        TransactionType type = TransactionType.DEBIT;

        if (amounts.size() >= 1) {
            // In IPPB, if it's a withdrawal, particulars end, then amount, then balance.
            // If it's a deposit, particulars end, then amount, then balance.
            // We can check block for ~DR~ or ~CR~
            amount = new BigDecimal(amounts.get(0).replace(",", ""));
            if (block.contains("~CR~") || block.contains("Credit") || block.contains("Deposit") || block.contains("DEPOSIT")) {
                type = TransactionType.CREDIT;
            } else {
                type = TransactionType.DEBIT;
            }
            
            // Description is everything between date and the first amount
            String descText = remaining;
            int firstAmountStart = block.indexOf(amounts.get(0));
            // Adjust relative to remaining
            int relativeStart = remaining.indexOf(amounts.get(0));
            if (relativeStart > 0) {
                descText = remaining.substring(0, relativeStart).trim();
            }
            
            // Cleanup description (remove transaction IDs like S94244669)
            descText = descText.replaceAll("S\\d{8,10}", "").trim();
            dto.setRawDescription(descText);
            dto.setAmount(amount);
            dto.setType(type);
            list.add(dto);
        }
    }

    private LocalDate parseDate(String dateStr) {
        try {
            if (dateStr.contains("-")) {
                // Handle DD-MM-YYYY or DD-MMM-YYYY
                if (dateStr.matches("\\d{2}-[A-Za-z]{3}-\\d{4}")) {
                    return LocalDate.parse(dateStr, dtf3);
                }
                return LocalDate.parse(dateStr, dtf2);
            } else {
                return LocalDate.parse(dateStr, dtf1);
            }
        } catch (Exception e) {
            return LocalDate.now();
        }
    }

    private BigDecimal parseAmount(String amountStr) {
        return new BigDecimal(amountStr.replace(",", ""));
    }
}

