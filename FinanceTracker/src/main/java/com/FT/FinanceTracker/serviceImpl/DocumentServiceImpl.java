package com.FT.FinanceTracker.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.FT.FinanceTracker.dto.DashboardResponseDto;
import com.FT.FinanceTracker.dto.MerchantInfo;
import com.FT.FinanceTracker.dto.TransactionDto;
import com.FT.FinanceTracker.entity.Transaction;
import com.FT.FinanceTracker.entity.User;
import com.FT.FinanceTracker.repository.TransactionRepository;
import com.FT.FinanceTracker.repository.UserRepository;
import com.FT.FinanceTracker.service.BudgetEvaluationService;
import com.FT.FinanceTracker.service.CategorizationService;
import com.FT.FinanceTracker.service.ConfidanceService;
import com.FT.FinanceTracker.service.DashboardAggregationService;
import com.FT.FinanceTracker.service.DocumentProcessingService;
import com.FT.FinanceTracker.service.MerchantNormalizationService;
import com.FT.FinanceTracker.service.ParsingService;
import com.FT.FinanceTracker.service.RecurringDetectionService;
import com.FT.FinanceTracker.service.TransactionExtractionService;
import com.FT.FinanceTracker.utils.TransactionMapper;

import jakarta.transaction.Transactional;

@Service
public class DocumentServiceImpl implements DocumentProcessingService {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DocumentServiceImpl.class);
    private final UserRepository userRepository;
    private final ParsingService parsingService;
    private final TransactionExtractionService transactionExtractionService;
    private final MerchantNormalizationService merchantNormalizationService;
    private final CategorizationService categorizationService;
    private final ConfidanceService confidenceService;
    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;
    private final RecurringDetectionService recurringDetectionService;
    private final BudgetEvaluationService budgetEvaluationService;
    private final DashboardAggregationService dashboardAggregationService;
    private final com.FT.FinanceTracker.service.SystemLogService logService;

    public DocumentServiceImpl(UserRepository userRepository,
                                ParsingService parsingService,
                                TransactionExtractionService transactionExtractionService,
                                MerchantNormalizationService merchantNormalizationService,
                                CategorizationService categorizationService,
                                ConfidanceService confidenceService,
                                TransactionMapper transactionMapper,
                                TransactionRepository transactionRepository,
                                RecurringDetectionService recurringDetectionService,
                                BudgetEvaluationService budgetEvaluationService,
                                DashboardAggregationService dashboardAggregationService,
                                com.FT.FinanceTracker.service.SystemLogService logService) {
        this.userRepository = userRepository;
        this.parsingService = parsingService;
        this.transactionExtractionService = transactionExtractionService;
        this.merchantNormalizationService = merchantNormalizationService;
        this.categorizationService = categorizationService;
        this.confidenceService = confidenceService;
        this.transactionMapper = transactionMapper;
        this.transactionRepository = transactionRepository;
        this.recurringDetectionService = recurringDetectionService;
        this.budgetEvaluationService = budgetEvaluationService;
        this.dashboardAggregationService = dashboardAggregationService;
        this.logService = logService;
    }

    @Override
    @Transactional
    public DashboardResponseDto processDocument(MultipartFile file, String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        String extractedText = parsingService.extractText(file);

        List<TransactionDto> extractedTransactions =
                transactionExtractionService.extractTransactions(extractedText);
        
        logger.info("Extracted {} transactions from document for user {}", extractedTransactions.size(), userEmail);

        List<Transaction> enrichedTransactions = new ArrayList<>();

        for (TransactionDto dto : extractedTransactions) {
            MerchantInfo merchantInfo =
                    merchantNormalizationService.normalize(dto.getRawDescription());

            String category =
                    categorizationService.categorize(dto, merchantInfo);

            double confidence =
                    confidenceService.calculate(dto, merchantInfo, category);

            Transaction transaction =
                    transactionMapper.toEntity(dto, merchantInfo, category, confidence, user);

            enrichedTransactions.add(transaction);
        }

        transactionRepository.saveAll(enrichedTransactions);

        recurringDetectionService.detectRecurring(user);

        budgetEvaluationService.evaluate(user);

        logService.info("Processed statement " + file.getOriginalFilename() + " for user " + userEmail + ". Extracted " + extractedTransactions.size() + " transactions.", "DOCUMENT_SERVICE");

        return dashboardAggregationService.buildDashboard(user);
    }
}
