package com.f1dashboard.backend.dto;

public class PitStopRequest {

    private int position;

    private String tire;

    private int lapsRemaining;

    public PitStopRequest() {
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTire() {
        return tire;
    }

    public void setTire(String tire) {
        this.tire = tire;
    }

    public int getLapsRemaining() {
        return lapsRemaining;
    }

    public void setLapsRemaining(int lapsRemaining) {
        this.lapsRemaining = lapsRemaining;
    }
}