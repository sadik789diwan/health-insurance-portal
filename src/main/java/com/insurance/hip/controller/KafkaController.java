package com.insurance.hip.controller;


import com.insurance.hip.entity.UserEvent;
import com.insurance.hip.service.UserEventProducer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kafka")
public class KafkaController {

    private final UserEventProducer producer;

    public KafkaController(UserEventProducer producer) {
        this.producer = producer;
    }

    @PostMapping("/publish")
    public String publish(@RequestBody UserEvent event) {
        producer.sendMessage(event);
        return "Message published successfully!";
    }
}
