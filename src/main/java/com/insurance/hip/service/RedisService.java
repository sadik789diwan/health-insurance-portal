package com.insurance.hip.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = new ObjectMapper();
    }

    // -------------------- String operations --------------------
    public void saveString(String key, Object value) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(value);
        redisTemplate.opsForValue().set(key, json);
    }

    public <T> T getString(String key, Class<T> clazz) throws JsonProcessingException {
        String json = (String) redisTemplate.opsForValue().get(key);
        if (json == null) return null;
        return objectMapper.readValue(json, clazz);
    }

    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    // -------------------- Set operations --------------------
    public void addToSet(String setName, String value) {
        redisTemplate.opsForSet().add(setName, value);
    }

    public Set<Object> getSetMembers(String setName) {
        return redisTemplate.opsForSet().members(setName);
    }

    public void removeFromSet(String setName, String value) {
        redisTemplate.opsForSet().remove(setName, value);
    }
}
