package com.example.orderService.service;

import org.springframework.stereotype.Service;
import no.finn.unleash.Unleash;

@Service
public class FeatureFlagService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FeatureFlagService.class);

    private final Unleash unleash;

    public FeatureFlagService(Unleash unleash) {
        this.unleash = unleash;
    }

    public boolean isOrderNotificationsEnabled() {
        try {
            return unleash.isEnabled("order-notifications", false);
        } catch (Exception e) {
            log.warn("Unleash check failed for order-notifications, defaulting to false", e);
            return false;
        }
    }

    public boolean isBulkOrderDiscountEnabled() {
        try {
            return unleash.isEnabled("bulk-order-discount", false);
        } catch (Exception e) {
            log.warn("Unleash check failed for bulk-order-discount, defaulting to false", e);
            return false;
        }
    }
}