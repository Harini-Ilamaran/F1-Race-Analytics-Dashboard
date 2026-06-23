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
import java.util.HashMap;
import java.util.Map;

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

            JsonNode races =
                    raceRoot.path("MRData")
                            .path("RaceTable")
                            .path("Races");

            JsonNode qualifyingRaces =
                    qualifyingRoot.path("MRData")
                            .path("RaceTable")
                            .path("Races");

            System.out.println("Total races = " + races.size());

            for(JsonNode race : races){
                System.out.println(
                        race.path("raceName").asText()
                );
            }

            driverRepository.findAll()
                    .forEach(d -> {

                        d.setPodiums(0);

                        d.setPoles(0);

                        d.setFastestLaps(0);

                        driverRepository.save(d);

                    });

            for(JsonNode race : races){

                JsonNode results =
                        race.path("Results");

                for(JsonNode result : results){

                    int position =
                            result.path("position")
                                    .asInt();

                    if(position == 1){

                        System.out.println(
                                result.toPrettyString()
                        );
                    }

                    if(position <= 3){

                        String code =
                                result.path("Driver")
                                        .path("code")
                                        .asText();

                        System.out.println(
                                code + " - Position: " + position
                        );

                        Driver driver =
                                driverRepository
                                        .findByDriverCode(code);

                        if(driver != null){

                            driver.setPodiums(
                                    driver.getPodiums() + 1
                            );

                            driverRepository.save(driver);

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

                        Driver driver =
                                driverRepository
                                        .findByDriverCode(code);

                        if(driver != null){

                            driver.setFastestLaps(
                                    driver.getFastestLaps() + 1
                            );

                            driverRepository.save(driver);

                            System.out.println(
                                    "Fastest Lap: "
                                            + driver.getName()
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

                    Driver driver =
                            driverRepository
                                    .findByDriverCode(code);

                    if(driver != null){

                        driver.setPoles(
                                driver.getPoles() + 1
                        );

                        driverRepository.save(driver);

                        System.out.println(
                                "Pole: " +
                                        driver.getName()
                        );
                    }
                }
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

                Driver driver =
                        driverRepository
                                .findByDriverCode(code);

                if(driver != null){

                    driver.setPoints(points);

                    driver.setWins(wins);

                    driver.setNationality(nationality);

                    driverRepository.save(driver);

                    System.out.println(
                            "Updated: "
                                    + driver.getName()
                    );
                }

            }
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

            LocalDate today =
                    LocalDate.now();

            int racesCompleted = 0;

            JsonNode nextRace = null;

            for(JsonNode race : races){

                LocalDate raceDate =
                        LocalDate.parse(
                                race.path("date").asText()
                        );

                if(raceDate.isBefore(today)){

                    racesCompleted++;

                } else {

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
            }

            return result;

        }

        catch(Exception e){

            e.printStackTrace();

            return new HashMap<>();
        }
    }

    @Autowired
    private DriverRepository driverRepository;

    private final ObjectMapper objectMapper =
            new ObjectMapper();

}
