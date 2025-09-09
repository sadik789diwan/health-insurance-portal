package com.insurance.hip.controller;

import com.insurance.hip.service.DataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class DataController {

    private final DataService dataService;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/fetchAll")
    public CompletableFuture<String> fetchAll() throws Exception {

        CompletableFuture<String> user = dataService.fetchUser();
        CompletableFuture<String> orders = dataService.fetchOrders();
        CompletableFuture<String> payments = dataService.fetchPayments();

        // Run all in parallel & wait until all complete
        return CompletableFuture.allOf(user, orders, payments)
                .thenApply(v -> "Results => " +
                        user.join() + ", " +
                        orders.join() + ", " +
                        payments.join());
    }
//    Result => Java + Spring Boot + CompletableFuture
    @GetMapping("/chain")
    public CompletableFuture<String> chainExample() {
        return CompletableFuture.supplyAsync(() -> "Java")
                .thenApply(str -> str + " + Spring Boot")
                .thenApply(str -> str + " + CompletableFuture")
                .thenApply(str -> "Result => " + str);
    }

    @GetMapping("/parallel")
    public CompletableFuture<String> parallelExample() throws Exception {
        CompletableFuture<String> user = CompletableFuture.supplyAsync(() -> "User Data");
        CompletableFuture<String> orders = CompletableFuture.supplyAsync(() -> "Orders Data");
        CompletableFuture<String> payments = CompletableFuture.supplyAsync(() -> "Payments Data");

        return CompletableFuture.allOf(user, orders, payments)
                .thenApply(v -> user.join() + " | " + orders.join() + " | " + payments.join());
    }

    @GetMapping("/combine")
    public CompletableFuture<String> combineExample() {
        CompletableFuture<String> first = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<String> second = CompletableFuture.supplyAsync(() -> "World");

        return first.thenCombine(second, (a, b) -> a + " " + b);
    }

    @GetMapping("/anyof")
    public CompletableFuture<Object> anyOfExample() {
        CompletableFuture<String> fast = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
            return "Fast Task";
        });

        CompletableFuture<String> slow = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
            }
            return "Slow Task";
        });

        return CompletableFuture.anyOf(fast, slow);
    }

    @GetMapping("/exception")
    public CompletableFuture<String> exceptionExample() {
        return CompletableFuture.supplyAsync(() -> {
            if (true) throw new RuntimeException("Something went wrong!");
            return "Success";
        }).exceptionally(ex -> "Recovered: " + ex.getMessage());
    }


}
