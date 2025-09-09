package com.insurance.hip.controller;


import com.insurance.hip.entity.TechStack;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class TechStackController {

    @GetMapping("/api/techstack")
    public ResponseEntity<List<TechStack>> getTechStack() {
        List<TechStack> techStack = Arrays.asList(
                new TechStack("Backend", "Spring Boot (Web, Data JPA, Security, Validation)"),
                new TechStack("Database", "PostgreSQL/MySQL"),
                new TechStack("Authentication", "JWT (Spring Security)"),
                new TechStack("Build Tool", "Maven/Gradle"),
                new TechStack("API Docs", "Swagger/OpenAPI"),
                new TechStack("Optional for Microservices", "Spring Cloud (Eureka, Config, Gateway)")
        );

        return ResponseEntity.ok(techStack);
    }
}
