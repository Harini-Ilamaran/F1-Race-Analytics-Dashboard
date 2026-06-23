package com.f1dashboard.backend.controller;

import com.f1dashboard.backend.dto.PitStopRequest;
import com.f1dashboard.backend.dto.PitStopResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pitstop")
@CrossOrigin("*")
public class PitStopController {

    @PostMapping
    public PitStopResponse calculateStrategy(
            @RequestBody PitStopRequest request) {

        String strategy;
        String pitWindow;
        String estimatedFinish;

        if(request.getTire().equalsIgnoreCase("SOFT")
                && request.getLapsRemaining() > 20) {

            strategy = "2 Stop Strategy";
            pitWindow = "Lap 15";
            estimatedFinish = "P2";
        }

        else if(request.getTire().equalsIgnoreCase("MEDIUM")) {

            strategy = "1 Stop Strategy";
            pitWindow = "Lap 20";
            estimatedFinish = "P3";
        }

        else {

            strategy = "Long Stint Strategy";
            pitWindow = "Lap 25";
            estimatedFinish = "P4";
        }

        return new PitStopResponse(
                strategy,
                pitWindow,
                estimatedFinish
        );
    }
}