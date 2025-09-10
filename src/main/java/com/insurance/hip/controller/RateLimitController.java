package com.insurance.hip.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api")
public class RateLimitController {

    private final Map<String, AtomicInteger> requests = new ConcurrentHashMap<>();

    @GetMapping("/limited")
    public ResponseEntity<String> limited() {
        String client = "user"; // identify client
        requests.putIfAbsent(client, new AtomicInteger(0));
        if(requests.get(client).incrementAndGet() > 5) { // max 5 requests
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate limit exceeded");
        }
        return ResponseEntity.ok("Success");
    }
}
