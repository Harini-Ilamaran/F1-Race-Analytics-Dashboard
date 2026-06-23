package com.f1dashboard.backend.controller;

import com.f1dashboard.backend.dto.TireRequest;
import com.f1dashboard.backend.dto.TireResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tire")
@CrossOrigin("*")
public class TireController {

    @PostMapping
    public TireResponse predictTireLife(
            @RequestBody TireRequest request) {

        String performanceDecline;
        String tireLife;
        String pitWindow;

        switch (request.getTireType()) {

            case "SOFT":
                performanceDecline = "High";
                tireLife = "15 Laps";
                pitWindow = "Lap 12";
                break;

            case "MEDIUM":
                performanceDecline = "Medium";
                tireLife = "25 Laps";
                pitWindow = "Lap 20";
                break;

            default:
                performanceDecline = "Low";
                tireLife = "35 Laps";
                pitWindow = "Lap 28";
        }

        return new TireResponse(
                performanceDecline,
                tireLife,
                pitWindow
        );
    }
}