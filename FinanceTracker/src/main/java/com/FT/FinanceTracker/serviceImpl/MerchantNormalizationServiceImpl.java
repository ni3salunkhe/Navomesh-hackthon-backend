package com.FT.FinanceTracker.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.FT.FinanceTracker.dto.MerchantInfo;
import com.FT.FinanceTracker.entity.MerchantAlias;
import com.FT.FinanceTracker.repository.MerchantAliasRepository;
import com.FT.FinanceTracker.service.MerchantNormalizationService;

@Service
public class MerchantNormalizationServiceImpl implements MerchantNormalizationService {

    private final MerchantAliasRepository merchantAliasRepository;

    public MerchantNormalizationServiceImpl(MerchantAliasRepository merchantAliasRepository) {
        this.merchantAliasRepository = merchantAliasRepository;
    }

    @Override
    public MerchantInfo normalize(String rawDescription) {
        List<MerchantAlias> aliases = merchantAliasRepository.findAllOrderedByPriority();

        for (MerchantAlias alias : aliases) {
            if (rawDescription.toUpperCase().contains(alias.getPattern().toUpperCase())) {
                return new MerchantInfo(
                    alias.getNormalizedName(),
                    alias.getDefaultCategory(),
                    0.5
                );
            }
        }

        // Fallback: use the raw description as the normalized name so that 
        // recurring transactions without aliases can still be clustered properly.
        return new MerchantInfo(rawDescription, "Uncategorized", 0.1);
    }
}
