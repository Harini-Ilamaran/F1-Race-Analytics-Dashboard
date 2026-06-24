package com.f1dashboard.backend;

import com.f1dashboard.backend.service.ConstructorSyncService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Autowired;
import com.f1dashboard.backend.service.DriverSyncService;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BackendApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Autowired
	private DriverSyncService driverSyncService;

	@Override
	public void run(String... args) {
//		constructorSyncService.syncConstructors();

	}

	@Autowired
	private ConstructorSyncService constructorSyncService;

}
