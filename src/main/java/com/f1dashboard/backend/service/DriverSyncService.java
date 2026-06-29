package com.f1dashboard.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.f1dashboard.backend.repository.DriverRepository;
import com.f1dashboard.backend.entity.Driver;
import org.springframework.scheduling.annotation.Scheduled;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class DriverSyncService {

    @Autowired
    private RestTemplate restTemplate;

    @Scheduled(fixedRate = 3600000)

    public void syncDrivers() {

        System.out.println(
                "Starting F1 Driver Sync..."
        );

        try {

            String response =
                    restTemplate.getForObject(
                            "https://api.jolpi.ca/ergast/f1/current/driverStandings.json",
                            String.class
                    );

            String raceResultsResponse =
                    restTemplate.getForObject(
                            "https://api.jolpi.ca/ergast/f1/current/results.json?limit=100",
                            String.class
                    );

            String qualifyingResponse =
                    restTemplate.getForObject(
                            "https://api.jolpi.ca/ergast/f1/current/qualifying.json?limit=100",
                            String.class
                    );

            JsonNode root =
                    objectMapper.readTree(response);

            JsonNode raceRoot =
                    objectMapper.readTree(
                            raceResultsResponse
                    );

            JsonNode qualifyingRoot =
                    objectMapper.readTree(
                            qualifyingResponse
                    );

            JsonNode standings =
                    root.path("MRData")
                            .path("StandingsTable")
                            .path("StandingsLists")
                            .get(0)
                            .path("DriverStandings");

            Map<String, Driver> driversByCode = new HashMap<>();

            List<Driver> updatedDrivers = new ArrayList<>();

            driverRepository.findAll().forEach(driver ->
                    driversByCode.put(driver.getDriverCode(), driver)
            );

            int leaderPoints =
                    standings.get(0)
                            .path("points")
                            .asInt();

            int highestWins = 0;
            int highestPodiums = 0;
            int highestPoles = 0;
            int highestFastestLaps = 0;

            JsonNode races =
                    raceRoot.path("MRData")
                            .path("RaceTable")
                            .path("Races");

            JsonNode qualifyingRaces =
                    qualifyingRoot.path("MRData")
                            .path("RaceTable")
                            .path("Races");

            driversByCode.values().forEach(d -> {

                d.setPodiums(0);
                d.setPoles(0);
                d.setFastestLaps(0);

            });

            for(JsonNode race : races){

                JsonNode results =
                        race.path("Results");

                for(JsonNode result : results){

                    int position =
                            result.path("position")
                                    .asInt();


                    if(position <= 3){

                        String code =
                                result.path("Driver")
                                        .path("code")
                                        .asText();


                        Driver driver = driversByCode.get(code);

                        if(driver != null){

                            driver.setPodiums(
                                    driver.getPodiums() + 1
                            );

                        }
                    }
                }
            }

            for(JsonNode race : races){

                JsonNode results =
                        race.path("Results");

                for(JsonNode result : results){

                    String rank =
                            result.path("FastestLap")
                                    .path("rank")
                                    .asText();

                    if(rank.equals("1")){

                        String code =
                                result.path("Driver")
                                        .path("code")
                                        .asText();

                        Driver driver = driversByCode.get(code);

                        if(driver != null){

                            driver.setFastestLaps(
                                    driver.getFastestLaps() + 1
                            );

                        }
                    }
                }
            }

            for(JsonNode race : qualifyingRaces){

                JsonNode qualifyingResults =
                        race.path("QualifyingResults");

                if(qualifyingResults.size() > 0){

                    JsonNode poleWinner =
                            qualifyingResults.get(0);

                    String code =
                            poleWinner.path("Driver")
                                    .path("code")
                                    .asText();

                    Driver driver = driversByCode.get(code);

                    if(driver != null){

                        driver.setPoles(
                                driver.getPoles() + 1
                        );


                    }
                }
            }

            for (Driver d : driversByCode.values()) {

                highestWins = Math.max(highestWins, d.getWins());

                highestPodiums = Math.max(highestPodiums, d.getPodiums());

                highestPoles = Math.max(highestPoles, d.getPoles());

                highestFastestLaps = Math.max(highestFastestLaps, d.getFastestLaps());

            }

            for(JsonNode standing : standings){

                String code =
                        standing.path("Driver")
                                .path("code")
                                .asText();

                int points =
                        standing.path("points")
                                .asInt();

                int wins =
                        standing.path("wins")
                                .asInt();

                String nationality =
                        standing.path("Driver")
                                .path("nationality")
                                .asText();

                Driver driver = driversByCode.get(code);

                if(driver != null){

                    driver.setPoints(points);

                    driver.setWins(wins);

                    driver.setNationality(nationality);

                    updateFantasyPrice(
                            driver,
                            leaderPoints,
                            highestWins,
                            highestPodiums,
                            highestPoles,
                            highestFastestLaps,
                            points,
                            wins,
                            driver.getPodiums(),
                            driver.getPoles(),
                            driver.getFastestLaps()
                    );

                    updatedDrivers.add(driver);

                }

            }

            driverRepository.saveAll(updatedDrivers);

            System.out.println(
                    "Driver Sync Completed"
            );

        }

        catch(Exception e){

            e.printStackTrace();

        }
    }

    public Map<String, Object> getRaceInfo() {

        try {

            String response =
                    restTemplate.getForObject(
                            "https://api.jolpi.ca/ergast/f1/current/races.json",
                            String.class
                    );

            JsonNode root =
                    objectMapper.readTree(response);

            JsonNode races =
                    root.path("MRData")
                            .path("RaceTable")
                            .path("Races");

            LocalDateTime now =
                    LocalDateTime.now();

            int racesCompleted = 0;

            JsonNode nextRace = null;

            for(JsonNode race : races){

                String raceDate =
                        race.path("date").asText();

                String raceTime =
                        race.path("time").asText();

                OffsetDateTime raceDateTime =
                        OffsetDateTime.parse(
                                raceDate + "T" + raceTime
                        );

                LocalDateTime localRaceTime =
                        raceDateTime
                                .atZoneSameInstant(
                                        java.time.ZoneId.systemDefault()
                                )
                                .toLocalDateTime();

                if(localRaceTime.isBefore(now)){

                    racesCompleted++;

                }else{

                    nextRace = race;

                    break;

                }
            }

            Map<String,Object> result =
                    new HashMap<>();

            result.put(
                    "racesCompleted",
                    racesCompleted
            );

            if(nextRace != null){

                result.put(
                        "raceName",
                        nextRace.path("raceName").asText()
                );

                result.put(
                        "circuit",
                        nextRace.path("Circuit")
                                .path("circuitName")
                                .asText()
                );

                result.put(
                        "raceDate",
                        nextRace.path("date").asText()
                );

                result.put(
                        "raceTime",
                        nextRace.path("time").asText()
                );
            }

            return result;

        }

        catch(Exception e){

            Map<String,Object> error =
                    new HashMap<>();

            error.put(
                    "error",
                    e.getClass().getName()
            );

            error.put(
                    "message",
                    e.getMessage()
            );

            return error;
        }
    }

    @Autowired
    private DriverRepository driverRepository;

    private final ObjectMapper objectMapper =
            new ObjectMapper();

    private void updateFantasyPrice(
            Driver driver,
            int leaderPoints,
            int highestWins,
            int highestPodiums,
            int highestPoles,
            int highestFastestLaps,
            int newPoints,
            int newWins,
            int newPodiums,
            int newPoles,
            int newFastestLaps
    ){

        double rating = 0;

// Championship points contribute the most
        rating += newPoints * 0.12;

// Race wins
        rating += newWins * 8;

        rating += newPodiums * 3;

        rating += newPoles * 2;

        rating += newFastestLaps * 1.5;

        rating = Math.min(rating, 100);

        driver.setPerformanceScore(rating);

        double previousRating = driver.getLastPerformanceScore();

        double ratingDifference = rating - previousRating;

        double change = ratingDifference * 0.15;

        if (change > 0.3) {
            change = 0.3;
        }

        if (change < -0.3) {
            change = -0.3;
        }

        double targetPrice = 5 + (Math.sqrt(rating / 100.0) * 25);
        double currentPrice = driver.getCost();

        double difference = targetPrice - currentPrice;

        if (difference > 0.3) {
            change = 0.3;
        }
        else if (difference < -0.3) {
            change = -0.3;
        }
        else {
            change = difference;
        }

        double updatedPrice = currentPrice + change;

        if (updatedPrice < 5) {
            updatedPrice = 5;
        }

        if (updatedPrice > 30) {
            updatedPrice = 30;
        }

        updatedPrice = Math.round(updatedPrice * 10.0) / 10.0;
        change = Math.round(change * 10.0) / 10.0;

        driver.setPriceChange(change);
        driver.setCost(updatedPrice);

        driver.setLastPerformanceScore(rating);

        driver.setLastWins(newWins);

        driver.setLastPodiums(newPodiums);

        driver.setLastPoles(newPoles);

        driver.setLastFastestLaps(newFastestLaps);

    }

}
