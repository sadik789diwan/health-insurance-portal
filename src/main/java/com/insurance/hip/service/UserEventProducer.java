package com.insurance.hip.service;


import com.insurance.hip.entity.UserEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserEventProducer {

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    public UserEventProducer(KafkaTemplate<String, UserEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(UserEvent event) {
        kafkaTemplate.send("user-events", event);
        System.out.println("Sent message: " + event.getName());
    }
}
