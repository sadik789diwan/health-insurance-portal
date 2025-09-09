package com.insurance.hip.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class DataService {

    @Async
    public CompletableFuture<String> fetchUser() throws InterruptedException {
        Thread.sleep(2000); // simulate delay
        return CompletableFuture.completedFuture("User Data");
    }

    @Async
    public CompletableFuture<String> fetchOrders() throws InterruptedException {
        Thread.sleep(3000);
        return CompletableFuture.completedFuture("Orders Data");
    }

    @Async
    public CompletableFuture<String> fetchPayments() throws InterruptedException {
        Thread.sleep(10000);
        return CompletableFuture.completedFuture("Payments Data");
    }
}
