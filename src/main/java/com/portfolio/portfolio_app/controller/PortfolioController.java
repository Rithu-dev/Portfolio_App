
package com.portfolio.portfolio_app.controller;

import java.util.List;

import com.portfolio.portfolio_app.dto.PortfolioSummaryDTO;
import com.portfolio.portfolio_app.entity.Portfolio;
import com.portfolio.portfolio_app.entity.User;
import com.portfolio.portfolio_app.service.PortfolioService;
import com.portfolio.portfolio_app.service.CurrentUserService;

import jakarta.validation.Valid;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final CurrentUserService currentUserService;

    public PortfolioController(
            PortfolioService portfolioService,
            CurrentUserService currentUserService) {
        this.portfolioService = portfolioService;
        this.currentUserService = currentUserService;
    }

    @PostMapping
    public Portfolio addStock(@Valid @RequestBody Portfolio portfolio) {
        User user = currentUserService.getCurrentUser();
        return portfolioService.addStock(portfolio, user);
    }

    @GetMapping("/user/{userId}")
    public List<Portfolio> getPortfolio(@PathVariable Long userId) {

        Long currentUserId = currentUserService.getCurrentUserId();

        if (!currentUserId.equals(userId)) {
            throw new AccessDeniedException("You cannot access another user's portfolio");
        }

        return portfolioService.getUserPortfolio(currentUserId);
    }

    @GetMapping("/user/{userId}/summary")
    public PortfolioSummaryDTO getPortfolioSummary(@PathVariable Long userId) {

        Long currentUserId = currentUserService.getCurrentUserId();

        if (!currentUserId.equals(userId)) {
            throw new AccessDeniedException("You cannot access another user's portfolio");
        }

        return portfolioService.getPortfolioSummary(currentUserId);
    }
}
