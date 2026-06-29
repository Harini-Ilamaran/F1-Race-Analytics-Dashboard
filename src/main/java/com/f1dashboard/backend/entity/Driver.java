package com.f1dashboard.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "drivers")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String team;

    private int points;

    private int wins;

    private int podiums;

    private int poles;

    private int fastestLaps;

    private double basePrice;

    private double cost;

    private String driverCode;

    private String nationality;

    private String imageFile;

    private int lastPoints;

    private int lastWins;

    private int lastPodiums;

    private int lastPoles;

    private int lastFastestLaps;

    private double priceChange;

    private double performanceScore;

    private double lastPerformanceScore;


    public Driver() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getPodiums() {
        return podiums;
    }

    public void setPodiums(int podiums) {
        this.podiums = podiums;
    }

    public int getPoles() {
        return poles;
    }

    public void setPoles(int poles) {
        this.poles = poles;
    }

    public int getFastestLaps() {
        return fastestLaps;
    }

    public void setFastestLaps(int fastestLaps) {
        this.fastestLaps = fastestLaps;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getDriverCode() {
        return driverCode;
    }

    public void setDriverCode(String driverCode) {
        this.driverCode = driverCode;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public int getLastPoints() {
        return lastPoints;
    }

    public void setLastPoints(int lastPoints) {
        this.lastPoints = lastPoints;
    }

    public int getLastWins() {
        return lastWins;
    }

    public void setLastWins(int lastWins) {
        this.lastWins = lastWins;
    }

    public int getLastPodiums() {
        return lastPodiums;
    }

    public void setLastPodiums(int lastPodiums) {
        this.lastPodiums = lastPodiums;
    }

    public int getLastPoles() {
        return lastPoles;
    }

    public void setLastPoles(int lastPoles) {
        this.lastPoles = lastPoles;
    }

    public int getLastFastestLaps() {
        return lastFastestLaps;
    }

    public void setLastFastestLaps(int lastFastestLaps) {
        this.lastFastestLaps = lastFastestLaps;
    }

    public double getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(double priceChange) {
        this.priceChange = priceChange;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public double getPerformanceScore() {
        return performanceScore;
    }

    public void setPerformanceScore(double performanceScore) {
        this.performanceScore = performanceScore;
    }

    public double getLastPerformanceScore() {
        return lastPerformanceScore;
    }

    public void setLastPerformanceScore(double lastPerformanceScore) {
        this.lastPerformanceScore = lastPerformanceScore;
    }
}