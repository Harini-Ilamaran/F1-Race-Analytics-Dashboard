package com.f1dashboard.backend.repository;

import com.f1dashboard.backend.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository
        extends JpaRepository<Driver, Long> {

    Driver findByName(String name);

    Driver findByDriverCode(String driverCode);
}
