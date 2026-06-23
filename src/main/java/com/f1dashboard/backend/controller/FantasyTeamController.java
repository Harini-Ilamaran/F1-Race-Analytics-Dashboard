package com.f1dashboard.backend.controller;

import com.f1dashboard.backend.dto.FantasyTeamRequest;
import com.f1dashboard.backend.dto.FantasyTeamResponse;
import com.f1dashboard.backend.entity.Driver;
import com.f1dashboard.backend.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fantasy")
@CrossOrigin("*")
public class FantasyTeamController {

    @Autowired
    private DriverRepository driverRepository;

    private static final double BUDGET = 100.0;

    @PostMapping
    public FantasyTeamResponse buildTeam(
            @RequestBody FantasyTeamRequest request) {

        Driver driver1 =
                driverRepository.findById(
                                request.getDriver1Id())
                        .orElseThrow();

        Driver driver2 =
                driverRepository.findById(
                                request.getDriver2Id())
                        .orElseThrow();

        double constructorCost;
        int constructorPoints;

        switch (request.getConstructorName()) {

            case "McLaren":
                constructorCost = 30;
                constructorPoints = 374;
                break;

            case "Ferrari":
                constructorCost = 28;
                constructorPoints = 285;
                break;

            case "Mercedes":
                constructorCost = 25;
                constructorPoints = 199;
                break;

            case "Red Bull Racing":
                constructorCost = 26;
                constructorPoints = 203;
                break;

            case "Williams":
                constructorCost = 18;
                constructorPoints = 98;
                break;

            case "Aston Martin":
                constructorCost = 17;
                constructorPoints = 78;
                break;

            case "RB":
                constructorCost = 16;
                constructorPoints = 40;
                break;

            case "Haas":
                constructorCost = 15;
                constructorPoints = 35;
                break;

            case "Alpine":
                constructorCost = 14;
                constructorPoints = 30;
                break;

            case "Kick Sauber":
                constructorCost = 12;
                constructorPoints = 29;
                break;

            default:
                constructorCost = 20;
                constructorPoints = 50;
        }

        double totalCost =
                driver1.getCost()
                        + driver2.getCost()
                        + constructorCost;

        int predictedPoints =
                driver1.getPoints()
                        + driver2.getPoints()
                        + constructorPoints;

        double budgetRemaining =
                BUDGET - totalCost;

        String status =
                totalCost <= BUDGET
                        ? "VALID TEAM ✅"
                        : "OVER BUDGET ❌";

        return new FantasyTeamResponse(
                totalCost,
                predictedPoints,
                budgetRemaining,
                status
        );
    }
}