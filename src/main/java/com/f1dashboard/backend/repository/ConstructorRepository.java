package com.f1dashboard.backend.repository;

import com.f1dashboard.backend.entity.Constructor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConstructorRepository extends JpaRepository<Constructor, Long> {

    Constructor findByName(String name);

}