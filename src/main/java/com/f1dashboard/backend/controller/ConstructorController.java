package com.f1dashboard.backend.controller;

import com.f1dashboard.backend.entity.Constructor;
import com.f1dashboard.backend.repository.ConstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/constructors")
public class ConstructorController {

    @Autowired
    private ConstructorRepository constructorRepository;

    @GetMapping
    public List<Constructor> getConstructors() {

        return constructorRepository.findAll();

    }
}