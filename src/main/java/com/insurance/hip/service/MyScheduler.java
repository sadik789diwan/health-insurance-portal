package com.insurance.hip.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MyScheduler {

    @Scheduled(cron = "0 */5 * * * *")
    public void runEveryFiveMinutes(){
        System.out.println("Schedule triggered at: " + java.time.LocalDateTime.now());
    }
}
