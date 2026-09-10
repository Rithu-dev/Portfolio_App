package com.portfolio.portfolio_app.service;

import com.portfolio.portfolio_app.dto.PortfolioSummaryDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.portfolio.portfolio_app.entity.Portfolio;
import com.portfolio.portfolio_app.repository.PortfolioRepository;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    public Portfolio addStock(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }

    public List<Portfolio> getUserPortfolio(Long userId) {
        return portfolioRepository.findByUserId(userId);
    }

    public PortfolioSummaryDTO getPortfolioSummary(Long userId) {

        List<Portfolio> portfolios = portfolioRepository.findByUserId(userId);

        int totalStocks = 0;
        double totalInvestment = 0;

        for (Portfolio portfolio : portfolios) {
            totalStocks += portfolio.getQuantity();
            totalInvestment += portfolio.getQuantity() * portfolio.getPrice();
        }

        PortfolioSummaryDTO summary = new PortfolioSummaryDTO();

        summary.setTotalStocks(totalStocks);
        summary.setTotalInvestment(totalInvestment);

        return summary;
    }
}
