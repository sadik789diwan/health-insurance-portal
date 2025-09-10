package com.insurance.hip.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.hip.entity.UserEvent;
import com.insurance.hip.entity.UserEventEntity;
import com.insurance.hip.repository.UserEventRepository;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

@Service
public class UserEventConsumer {

    private LinkedBlockingQueue<UserEvent> queue= new LinkedBlockingQueue<>(100);
    private final ExecutorService executorService= Executors.newFixedThreadPool(2);
    private final UserEventRepository repository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private static final int BATCH_SIZE=5;
    public UserEventConsumer(UserEventRepository repository, RedisTemplate<String, Object> redisTemplate) {
        this.repository = repository;
        this.redisTemplate = redisTemplate;
        this.objectMapper = new ObjectMapper(); // Jackson ObjectMapper
    }

    @KafkaListener(topics = "user-events", groupId = "my-group")
    public void consume(UserEvent event) {
        queue.offer(event);
    }

    @Scheduled(fixedRate = 1000)
    public void processBatch(){
        List<UserEvent> batch= new ArrayList<>();
        queue.drainTo(batch, BATCH_SIZE);
        if(!batch.isEmpty()){
            executorService.submit(()->saveBatchToDB(batch));
            executorService.submit(()->saveBatchToRedis(batch));
        }

    }
    private void saveBatchToDB(List<UserEvent> events) {
        List<UserEventEntity> entities = events.stream().map(e -> {
            UserEventEntity entity = new UserEventEntity();
            entity.setEventId(e.getId());
            entity.setName(e.getName());
            entity.setAction(e.getAction());
            return entity;
        }).collect(Collectors.toList());

        repository.saveAll(entities); // batch insert
        System.out.println("Saved batch to DB: " + entities.size());
    }
    private void saveBatchToRedis(List<UserEvent> events) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (UserEvent event : events) {
                try {
                    String key = "user-event:" + event.getId();
                    String jsonValue = objectMapper.writeValueAsString(event);
                    connection.set(key.getBytes(), jsonValue.getBytes());
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            return null;
        });
        System.out.println("Saved batch to Redis: " + events.size());
    }

    //@KafkaListener(topics = "user-events", groupId = "my-group")
    public void consumeOld(UserEvent event) {
        System.out.println("Consumed message: " + event.getName() + " - " + event.getAction());
        // Save into DB
        UserEventEntity entity = new UserEventEntity();
        entity.setEventId(event.getId());
        entity.setName(event.getName());
        entity.setAction(event.getAction());

        repository.save(entity);
        System.out.println("Saved to DB: " + event.getName());

//        // 2️⃣ Save to Redis using pipeline
//        List<Object> results = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
//            byte[] key = ("user-event:" + event.getId()).getBytes();
//            Map<byte[], byte[]> map = new HashMap<>();
//            map.put("id".getBytes(), event.getId().getBytes());
//            map.put("name".getBytes(), event.getName().getBytes());
//            map.put("action".getBytes(), event.getAction().getBytes());
//            connection.hMSet(key, map); // hMSet is deprecated but still works
//            return null;
//        });
//
//        System.out.println("Saved to Redis: " + event.getName() + ", pipeline results: " + results);

        // 2️⃣ Save to Redis as JSON string
        try {
            String key = "user-event:" + event.getId();
            String jsonValue = objectMapper.writeValueAsString(event);
            redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                connection.set(key.getBytes(), jsonValue.getBytes());
                return null;
            });
            System.out.println("Saved to Redis (JSON string): " + jsonValue);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
