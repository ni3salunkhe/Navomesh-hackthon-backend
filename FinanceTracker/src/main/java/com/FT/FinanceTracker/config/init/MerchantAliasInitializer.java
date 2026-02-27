package com.FT.FinanceTracker.config.init;

import com.FT.FinanceTracker.entity.MerchantAlias;
import com.FT.FinanceTracker.repository.MerchantAliasRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Order(2)
public class MerchantAliasInitializer implements CommandLineRunner {

    private final MerchantAliasRepository merchantAliasRepository;

    public MerchantAliasInitializer(MerchantAliasRepository merchantAliasRepository) {
        this.merchantAliasRepository = merchantAliasRepository;
    }

    @Override
    public void run(String... args) {
        if (merchantAliasRepository.count() > 0) {
            System.out.println("MERCHANT ALIASES ALREADY SEEDED");
            return;
        }

        List<MerchantAlias> aliases = Arrays.asList(
            new MerchantAlias(null, "McDonald's", "McDonald's", "FOOD", 10),
            new MerchantAlias(null, "KFC", "KFC", "FOOD", 10),
            new MerchantAlias(null, "Burger King", "Burger King", "FOOD", 10),
            new MerchantAlias(null, "Starbucks", "Starbucks", "FOOD", 10),
            new MerchantAlias(null, "Uber", "Uber", "TRANSPORT", 10),
            new MerchantAlias(null, "Ola", "Ola", "TRANSPORT", 10),
            new MerchantAlias(null, "Amazon", "Amazon", "SHOPPING", 10),
            new MerchantAlias(null, "Flipkart", "Flipkart", "SHOPPING", 10),
            new MerchantAlias(null, "Netflix", "Netflix", "ENTERTAINMENT", 10),
            new MerchantAlias(null, "Spotify", "Spotify", "ENTERTAINMENT", 10),
            new MerchantAlias(null, "Zomato", "Zomato", "FOOD", 10),
            new MerchantAlias(null, "Swiggy", "Swiggy", "FOOD", 10),
            new MerchantAlias(null, "Electricity", "Utility Bill", "BILLS", 10),
            new MerchantAlias(null, "Airtel", "Airtel", "BILLS", 10),
            new MerchantAlias(null, "Jio", "Jio", "BILLS", 10)
        );

        merchantAliasRepository.saveAll(aliases);
        System.out.println("MERCHANT ALIASES SEEDED: " + aliases.size() + " entries");
    }
}
