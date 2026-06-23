package com.f1dashboard.backend.dto;

public class PitStopResponse {

    private String strategy;

    private String pitWindow;

    private String estimatedFinish;

    public PitStopResponse() {
    }

    public PitStopResponse(
            String strategy,
            String pitWindow,
            String estimatedFinish) {

        this.strategy = strategy;
        this.pitWindow = pitWindow;
        this.estimatedFinish = estimatedFinish;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getPitWindow() {
        return pitWindow;
    }

    public void setPitWindow(String pitWindow) {
        this.pitWindow = pitWindow;
    }

    public String getEstimatedFinish() {
        return estimatedFinish;
    }

    public void setEstimatedFinish(String estimatedFinish) {
        this.estimatedFinish = estimatedFinish;
    }
}