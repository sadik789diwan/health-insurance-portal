package com.insurance.hip.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.insurance.hip.entity.UserEvent;
import com.insurance.hip.service.RedisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/redis")
public class RedisController {

    private final RedisService redisService;

    public RedisController(RedisService redisService) {
        this.redisService = redisService;
    }

    // -------------------- String endpoints --------------------
    @PostMapping("/string/{key}")
    public ResponseEntity<String> saveString(@PathVariable String key, @RequestBody UserEvent value) {
        try {
            redisService.saveString(key, value);
            return ResponseEntity.ok("Saved key: " + key);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/string/{key}")
    public ResponseEntity<UserEvent> getString(@PathVariable String key) {
        try {
            UserEvent value = redisService.getString(key, UserEvent.class);
            if (value == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(value);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/string/{key}")
    public ResponseEntity<String> deleteString(@PathVariable String key) {
        redisService.deleteKey(key);
        return ResponseEntity.ok("Deleted key: " + key);
    }

    // -------------------- Set endpoints --------------------
    @PostMapping("/set/{setName}")
    public ResponseEntity<String> addToSet(@PathVariable String setName, @RequestParam String value) {
        redisService.addToSet(setName, value);
        return ResponseEntity.ok("Added value to set: " + setName);
    }

    @GetMapping("/set/{setName}")
    public ResponseEntity<Set<Object>> getSet(@PathVariable String setName) {
        Set<Object> members = redisService.getSetMembers(setName);
        return ResponseEntity.ok(members);
    }

    @DeleteMapping("/set/{setName}")
    public ResponseEntity<String> removeFromSet(@PathVariable String setName, @RequestParam String value) {
        redisService.removeFromSet(setName, value);
        return ResponseEntity.ok("Removed value from set: " + setName);
    }
}
