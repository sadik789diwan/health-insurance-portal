package com.insurance.hip.repository;


import com.insurance.hip.entity.UserEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEventRepository extends JpaRepository<UserEventEntity, Long> {
}