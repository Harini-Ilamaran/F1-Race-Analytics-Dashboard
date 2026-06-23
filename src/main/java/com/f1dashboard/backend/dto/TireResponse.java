package com.f1dashboard.backend.dto;

public class TireResponse {

    private String performanceDecline;

    private String tireLife;

    private String pitWindow;

    public TireResponse() {
    }

    public TireResponse(
            String performanceDecline,
            String tireLife,
            String pitWindow) {

        this.performanceDecline = performanceDecline;
        this.tireLife = tireLife;
        this.pitWindow = pitWindow;
    }

    public String getPerformanceDecline() {
        return performanceDecline;
    }

    public String getTireLife() {
        return tireLife;
    }

    public String getPitWindow() {
        return pitWindow;
    }
}