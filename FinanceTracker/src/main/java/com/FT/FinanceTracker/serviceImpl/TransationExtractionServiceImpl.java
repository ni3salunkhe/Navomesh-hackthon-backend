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
public class TransationExtractionServiceImpl implements TransactionExtractionService {

    // Old format matcher: 12/01/2024 ... 1,500.00 CR
    private static final Pattern TRANSACTION_PATTERN_OLD =
            Pattern.compile("(\\d{2}/\\d{2}/\\d{4})\\s+(.*?)\\s+([0-9,]+\\.\\d{2})\\s+(CR|DR)");
            
    // New format matcher based on user data: TXN001 02-01-2025SBIN00012345Debit 13077 61923 Electricity Bill
    private static final Pattern TRANSACTION_PATTERN_NEW =
            Pattern.compile("TXN\\d+\\s+(\\d{2}-\\d{2}-\\d{4})\\S+\\s*(Credit|Debit)\\s+([0-9,]+(?:\\.\\d{2})?)\\s+[0-9,]+(?:\\.\\d{2})?\\s+(.*)");

    private static final DateTimeFormatter formatterOld = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter formatterNew = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public List<TransactionDto> extractTransactions(String rawText) {
        System.out.println("====== EXTRACTED PDF TEXT START =====");
        if (rawText != null) {
            System.out.println(rawText.length() > 1000 ? rawText.substring(0, 1000) : rawText);
        }
        System.out.println("====== EXTRACTED PDF TEXT END =======");
        
        List<TransactionDto> list = new ArrayList<>();
        
        // Try old format first
        Matcher matcherOld = TRANSACTION_PATTERN_OLD.matcher(rawText);
        while (matcherOld.find()) {
            TransactionDto dto = new TransactionDto();
            dto.setDate(LocalDate.parse(matcherOld.group(1), formatterOld));
            dto.setRawDescription(matcherOld.group(2).trim());
            dto.setAmount(parseAmount(matcherOld.group(3)));
            dto.setType(matcherOld.group(4).equals("CR") ? TransactionType.CREDIT : TransactionType.DEBIT);
            list.add(dto);
        }
        
        // If nothing found, try new format
        if (list.isEmpty()) {
            Matcher matcherNew = TRANSACTION_PATTERN_NEW.matcher(rawText);
            while (matcherNew.find()) {
                TransactionDto dto = new TransactionDto();
                dto.setDate(LocalDate.parse(matcherNew.group(1), formatterNew));
                dto.setRawDescription(matcherNew.group(4).trim());
                dto.setAmount(parseAmount(matcherNew.group(3)));
                dto.setType(matcherNew.group(2).equalsIgnoreCase("Credit") ? TransactionType.CREDIT : TransactionType.DEBIT);
                list.add(dto);
            }
        }

        return list;
    }

    private BigDecimal parseAmount(String amountStr) {
        return new BigDecimal(amountStr.replace(",", ""));
    }
}

