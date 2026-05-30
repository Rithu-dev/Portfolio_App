package com.portfolio.portfolio_app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.portfolio.portfolio_app.entity.Portfolio;
import com.portfolio.portfolio_app.service.PortfolioService;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    // Add stock
    @PostMapping
    public Portfolio addStock(@RequestBody Portfolio portfolio) {
        return portfolioService.addStock(portfolio);
    }

    // get user portfolio
    @GetMapping("/user/{userId}")
    public List<Portfolio> getPortfolio(@PathVariable Long userId) {
        return portfolioService.getUserPortfolio(userId);
    }

}
