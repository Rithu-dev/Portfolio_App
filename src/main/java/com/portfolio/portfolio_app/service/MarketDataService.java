
package com.portfolio.portfolio_app.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.portfolio.portfolio_app.dto.StockPriceResponse;

@Service
public class MarketDataService {

    private final RestClient restClient;

    public MarketDataService(RestClient marketDataRestClient) {
        this.restClient = marketDataRestClient;
    }

    public StockPriceResponse getStockPrice(String symbol) {

        String apiKey = System.getenv("TWELVE_DATA_API_KEY");
        // Check if the API key is present in the environment variables - 
        // Prevents calls without credentials
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Market data API key is missing");
        }

        //Rejects empty input
        if (symbol == null || symbol.isBlank()) {
            throw new IllegalArgumentException("Stock symbol cannot be empty");
        }

        try {
            StockPriceResponse response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/price")
                            .queryParam("symbol", symbol.trim().toUpperCase())
                            .queryParam("apikey", apiKey)
                            .build())
                    .retrieve()
                    .body(StockPriceResponse.class);

            if (response == null) {
                throw new IllegalStateException("Empty response received from market data provider");
            }

            // Twelve Data can return an error JSON with HTTP 200. That's why we explicitly
            // check response.getStatus() instead of relying only on HTTP status codes.
            if ("error".equalsIgnoreCase(response.getStatus())) {
                throw new IllegalArgumentException(
                        "Market data provider error: " + response.getMessage());
            }

            if (response.getPrice() == null || response.getPrice().isBlank()) {
                throw new IllegalStateException("Stock price not available");
            }

            return response;

        } catch (org.springframework.web.client.HttpClientErrorException ex) {

            throw new IllegalArgumentException(
                    "Invalid stock symbol or market data request: "
                            + ex.getResponseBodyAsString());

        } catch (RestClientException ex) {

            throw new IllegalStateException(
                    "Unable to communicate with market data provider", ex);
        }
    }
}
