package com.f1dashboard.backend.dto;

public class FantasyTeamRequest {

    private Long driver1Id;

    private Long driver2Id;

    private String constructorName;

    public FantasyTeamRequest() {
    }

    public Long getDriver1Id() {
        return driver1Id;
    }

    public void setDriver1Id(Long driver1Id) {
        this.driver1Id = driver1Id;
    }

    public Long getDriver2Id() {
        return driver2Id;
    }

    public void setDriver2Id(Long driver2Id) {
        this.driver2Id = driver2Id;
    }

    public String getConstructorName() {
        return constructorName;
    }

    public void setConstructorName(String constructorName) {
        this.constructorName = constructorName;
    }
}