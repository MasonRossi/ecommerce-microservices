package com.example.orderService.config;

import java.net.URI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import no.finn.unleash.DefaultUnleash;
import no.finn.unleash.Unleash;

@Configuration
public class UnleashConfig {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UnleashConfig.class);

    @Bean
    public Unleash unleash(
            @Value("${unleash.api-url}") String apiUrl,
            @Value("${unleash.api-token:}") String apiToken,
            @Value("${unleash.app-name:}") String appName
    ) {
        log.info("creating Unleash client (appName={}, apiUrl={})", appName, apiUrl);
        try {
            return new DefaultUnleash(URI.create(apiUrl));
        } catch (Exception e) {
            log.error("failed to create Unleash client, defaulting to noop", e);
            return new DefaultUnleash(URI.create(apiUrl));
        }
    }
}