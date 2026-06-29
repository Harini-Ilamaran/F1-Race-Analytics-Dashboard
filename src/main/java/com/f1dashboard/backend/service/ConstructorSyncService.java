package com.f1dashboard.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.f1dashboard.backend.entity.Constructor;
import com.f1dashboard.backend.repository.ConstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.scheduling.annotation.Scheduled;

@Service
public class ConstructorSyncService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ConstructorRepository constructorRepository;

    private final ObjectMapper objectMapper =
            new ObjectMapper();

    @Scheduled(fixedRate = 3600000)
    public void syncConstructors() {

        try {

            String response =
                    restTemplate.getForObject(
                            "https://api.jolpi.ca/ergast/f1/current/constructorStandings.json",
                            String.class
                    );

            JsonNode root =
                    objectMapper.readTree(response);

            JsonNode constructors =
                    root.path("MRData")
                            .path("StandingsTable")
                            .path("StandingsLists")
                            .get(0)
                            .path("ConstructorStandings");

            for(JsonNode standing : constructors){

                String name =
                        standing.path("Constructor")
                                .path("name")
                                .asText();

                int points =
                        standing.path("points")
                                .asInt();

                int wins =
                        standing.path("wins")
                                .asInt();

                String nationality =
                        standing.path("Constructor")
                                .path("nationality")
                                .asText();

                Constructor constructor =
                        constructorRepository.findByName(name);

                if(constructor == null){

                    constructor = new Constructor();
                    constructor.setName(name);
                }

                constructor.setPoints(points);
                constructor.setWins(wins);
                constructor.setNationality(nationality);

                double performanceScore =
                        (points * 0.80) +
                                (wins * 20);

                double price =
                        5 + (performanceScore / 30);

                if(price > 35){
                    price = 35;
                }

                if(price < 5){
                    price = 5;
                }

                price = Math.round(price * 10.0) / 10.0;

                constructor.setCost(price);

                constructorRepository.save(constructor);

                System.out.println(
                        "Updated Constructor: " + name
                );
            }

        } catch(Exception e){

            e.printStackTrace();

        }
    }
}