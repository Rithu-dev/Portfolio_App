
package com.portfolio.portfolio_app.controller;

import com.portfolio.portfolio_app.dto.TransactionRequest;
import com.portfolio.portfolio_app.entity.Transaction;
import com.portfolio.portfolio_app.entity.User;
import com.portfolio.portfolio_app.service.TransactionService;
import com.portfolio.portfolio_app.service.CurrentUserService;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final CurrentUserService currentUserService;

    public TransactionController(
            TransactionService transactionService,
            CurrentUserService currentUserService) {
        this.transactionService = transactionService;
        this.currentUserService = currentUserService;
    }

    @PostMapping
    public Transaction addTransaction(
            @RequestBody TransactionRequest request) {

        User user = currentUserService.getCurrentUser();

        Transaction transaction = new Transaction();

        transaction.setStockName(request.getStockName());
        transaction.setQuantity(request.getQuantity());
        transaction.setPrice(request.getPrice());
        transaction.setType(request.getType());

        return transactionService.addTransaction(transaction, user);
    }

    @GetMapping("/user/{userId}")
    public List<Transaction> getUserTransactions(
            @PathVariable Long userId) {

        Long currentUserId = currentUserService.getCurrentUserId();

        if (!currentUserId.equals(userId)) {
            throw new AccessDeniedException(
                    "You cannot access another user's transactions");
        }

        return transactionService.getUserTransactions(currentUserId);
    }
}
