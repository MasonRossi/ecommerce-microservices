package com.example.productService.service;

import org.springframework.stereotype.Service;
import no.finn.unleash.Unleash;

@Service
public class FeatureFlagService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FeatureFlagService.class);

    private final Unleash unleash;

    public FeatureFlagService(Unleash unleash) {
        this.unleash = unleash;
    }

    public boolean isPremiumPricingEnabled() {
        try {
            return unleash.isEnabled("premium-pricing", false);
        } catch (Exception e) {
            log.warn("Unleash check failed for premium-pricing, defaulting to false", e);
            return false;
        }
    }
}
