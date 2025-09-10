package com.insurance.hip.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_events")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventId;
    private String name;
    private String action;

    // getters & setters
}
