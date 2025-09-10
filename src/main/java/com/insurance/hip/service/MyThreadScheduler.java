package com.insurance.hip.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class MyThreadScheduler {
    private final ExecutorService executorService= Executors.newFixedThreadPool(100);

    @Scheduled(cron = "0 */55 * * * *")    //Every 5 minutes
    public void runThreadEveryFiveMinutes(){
        System.out.println("Starting 100 threads at: "+ java.time.LocalDateTime.now());
        for(int i=1; i<=100; i++){
            int threadId=i;
            executorService.submit(()->{
                System.out.println("Thread " + threadId+ " is running by "+ Thread.currentThread().getName());
            });
        }
    }
}
