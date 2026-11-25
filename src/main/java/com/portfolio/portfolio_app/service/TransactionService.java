package com.portfolio.portfolio_app.service;

import com.portfolio.portfolio_app.entity.Portfolio;
import com.portfolio.portfolio_app.repository.PortfolioRepository;
import com.portfolio.portfolio_app.entity.Transaction;
import com.portfolio.portfolio_app.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.portfolio.portfolio_app.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public Transaction addTransaction(Transaction transaction, User user) {

        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("Authenticated user is required");
        }

        transaction.setUser(user);

        if (transaction.getStockName() == null ||
                transaction.getStockName().isBlank()) {
            throw new IllegalArgumentException("Stock name is required");
        }

        if (transaction.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        if (transaction.getPrice() <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }

        if (transaction.getType() == null ||
                (!transaction.getType().equalsIgnoreCase("BUY") &&
                        !transaction.getType().equalsIgnoreCase("SELL"))) {
            throw new IllegalArgumentException("Transaction type must be BUY or SELL");
        }

        transaction.setTransactionDate(LocalDateTime.now());

        // Transaction savedTransaction = transactionRepository.save(transaction);

        Long userId = transaction.getUser().getId();

        List<Portfolio> portfolios = portfolioRepository.findByUserId(userId);

        Portfolio existingPortfolio = null;

        for (Portfolio portfolio : portfolios) {
            if (portfolio.getStockName().equalsIgnoreCase(transaction.getStockName())) {
                existingPortfolio = portfolio;
                break;
            }
        }

        if (transaction.getType().equalsIgnoreCase("BUY")) {

            if (existingPortfolio != null) {

                int totalQuantity = existingPortfolio.getQuantity() + transaction.getQuantity();

                double totalInvestment = (existingPortfolio.getQuantity() * existingPortfolio.getPrice())
                        + (transaction.getQuantity() * transaction.getPrice());

                double averagePrice = totalInvestment / totalQuantity;

                existingPortfolio.setQuantity(totalQuantity);
                existingPortfolio.setPrice(averagePrice);

                portfolioRepository.save(existingPortfolio);

            } else {

                Portfolio newPortfolio = new Portfolio();

                newPortfolio.setStockName(transaction.getStockName());
                newPortfolio.setQuantity(transaction.getQuantity());
                newPortfolio.setPrice(transaction.getPrice());
                newPortfolio.setUser(transaction.getUser());

                portfolioRepository.save(newPortfolio);
            }
        }

        if (transaction.getType().equalsIgnoreCase("SELL")) {

            if (existingPortfolio == null) {
                throw new RuntimeException("Stock not found in portfolio");
            }

            int remainingQuantity = existingPortfolio.getQuantity() - transaction.getQuantity();

            if (remainingQuantity < 0) {
                throw new RuntimeException("Not enough shares to sell");
            }

            existingPortfolio.setQuantity(remainingQuantity);

            if (remainingQuantity == 0) {
                portfolioRepository.delete(existingPortfolio);
            } else {
                portfolioRepository.save(existingPortfolio);
            }
        }

        Transaction savedTransaction = transactionRepository.save(transaction);
        return savedTransaction;
    }

    public List<Transaction> getUserTransactions(Long userId) {
        return transactionRepository.findByUserId(userId);
    }
}