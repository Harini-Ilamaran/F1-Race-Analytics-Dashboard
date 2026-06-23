package com.f1dashboard.backend.dto;

public class FantasyTeamResponse {

    private double totalCost;

    private int predictedPoints;

    private double budgetRemaining;

    private String status;

    public FantasyTeamResponse() {
    }

    public FantasyTeamResponse(
            double totalCost,
            int predictedPoints,
            double budgetRemaining,
            String status) {

        this.totalCost = totalCost;
        this.predictedPoints = predictedPoints;
        this.budgetRemaining = budgetRemaining;
        this.status = status;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public int getPredictedPoints() {
        return predictedPoints;
    }

    public double getBudgetRemaining() {
        return budgetRemaining;
    }

    public String getStatus() {
        return status;
    }
}