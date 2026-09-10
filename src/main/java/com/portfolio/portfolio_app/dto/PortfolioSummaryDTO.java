package com.portfolio.portfolio_app.dto;

public class PortfolioSummaryDTO {

    private int totalStocks;
    private double totalInvestment;

    public int getTotalStocks() {
        return totalStocks;
    }

    public void setTotalStocks(int totalStocks) {
        this.totalStocks = totalStocks;
    }

    public double getTotalInvestment() {
        return totalInvestment;
    }

    public void setTotalInvestment(double totalInvestment) {
        this.totalInvestment = totalInvestment;
    }
}