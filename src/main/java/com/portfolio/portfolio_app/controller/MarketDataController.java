package com.portfolio.portfolio_app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.portfolio_app.dto.StockPriceResponse;
import com.portfolio.portfolio_app.service.MarketDataService;

@RestController
@RequestMapping("/market")
public class MarketDataController {
    private final MarketDataService marketDataService;

    public MarketDataController(MarketDataService marketDataService) {
        this.marketDataService = marketDataService;
    }

    @GetMapping("/price")
    public StockPriceResponse getStockPrice(@RequestParam String symbol) {
        return marketDataService.getStockPrice(symbol);
    }
}
