package com.f1dashboard.backend.controller;

import com.f1dashboard.backend.entity.Driver;
import com.f1dashboard.backend.repository.DriverRepository;
import com.f1dashboard.backend.service.DriverSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@CrossOrigin("*")
public class DriverController {

    @Autowired
    private DriverRepository driverRepository;

    @GetMapping
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    @GetMapping("/{id}")
    public Driver getDriverById(@PathVariable Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Driver not found"));
    }

    @PostMapping
    public Driver addDriver(@RequestBody Driver driver) {
        return driverRepository.save(driver);
    }

    @GetMapping("/race-info")
    public Object getRaceInfo() {

        return driverSyncService.getRaceInfo();

    }

    @Autowired
    private DriverSyncService driverSyncService;
}