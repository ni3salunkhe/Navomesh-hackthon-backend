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

    private static final Pattern TRANSACTION_PATTERN =
            Pattern.compile("(\\d{2}/\\d{2}/\\d{4})\\s+(.*?)\\s+([0-9,]+\\.\\d{2})\\s+(CR|DR)");

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public List<TransactionDto> extractTransactions(String rawText) {
        List<TransactionDto> list = new ArrayList<>();
        Matcher matcher = TRANSACTION_PATTERN.matcher(rawText);

        while (matcher.find()) {
            TransactionDto dto = new TransactionDto();
            dto.setDate(LocalDate.parse(matcher.group(1), formatter));
            dto.setRawDescription(matcher.group(2).trim());
            dto.setAmount(parseAmount(matcher.group(3)));
            dto.setType(matcher.group(4).equals("CR") ? TransactionType.CREDIT : TransactionType.DEBIT);
            list.add(dto);
        }

        return list;
    }

    private BigDecimal parseAmount(String amountStr) {
        return new BigDecimal(amountStr.replace(",", ""));
    }
}
