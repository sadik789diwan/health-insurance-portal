package com.insurance.hip.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        "/api"
)
public class GreetingController {
    @GetMapping("/greet")
    public String greet(@RequestParam(defaultValue = "world") String name){
        return "Hello, "+ name +"!";
    }
}
