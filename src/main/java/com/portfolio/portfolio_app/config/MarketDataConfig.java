package com.portfolio.portfolio_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class MarketDataConfig {
    @Bean
    public RestClient marketDataRestClient() {
        return RestClient.builder()
                .baseUrl("https://api.twelvedata.com")
                // build() - Creates the configured RestClient instance.
                .build();
    }
}
