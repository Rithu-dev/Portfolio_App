package com.portfolio.portfolio_app.dto;

public record LoginRequest(
        String email,
        String password) {
}