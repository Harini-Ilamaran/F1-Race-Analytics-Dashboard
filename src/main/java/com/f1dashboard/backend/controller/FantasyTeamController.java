package com.f1dashboard.backend.controller;

import com.f1dashboard.backend.dto.FantasyTeamRequest;
import com.f1dashboard.backend.dto.FantasyTeamResponse;
import com.f1dashboard.backend.entity.Driver;
import com.f1dashboard.backend.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.f1dashboard.backend.entity.Constructor;
import com.f1dashboard.backend.repository.ConstructorRepository;
import java.util.List;

@RestController
@RequestMapping("/api/fantasy")
@CrossOrigin("*")
public class FantasyTeamController {

    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private ConstructorRepository constructorRepository;

    private static final double BUDGET = 100.0;

    @PostMapping
    public FantasyTeamResponse buildTeam(
            @RequestBody FantasyTeamRequest request) {

        List<Driver> selectedDrivers =
                driverRepository.findAllById(
                        List.of(
                                request.getDriver1Id(),
                                request.getDriver2Id()
                        )
                );

        if(selectedDrivers.size() != 2){
            throw new RuntimeException("Driver not found");
        }

        Driver driver1 = selectedDrivers.get(0);
        Driver driver2 = selectedDrivers.get(1);

        Constructor constructor =
                constructorRepository.findByName(
                        request.getConstructorName()
                );

        if (constructor == null) {
            throw new RuntimeException("Constructor not found");
        }

        double constructorCost = constructor.getCost();

        int constructorPoints = constructor.getPoints();

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
                        ? "VALID TEAM ✓"
                        : "OVER BUDGET X";

        return new FantasyTeamResponse(
                totalCost,
                predictedPoints,
                budgetRemaining,
                status
        );
    }
}