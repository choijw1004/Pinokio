package com.example.pinokkio.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import java.time.Duration;


@Slf4j
@RequiredArgsConstructor
@Service
public class RedisUtil {

    private final StringRedisTemplate template;

    public String getData(String key) {
        ValueOperations<String, String> valueOperations = template.opsForValue();
        return valueOperations.get(key);
    }

    public boolean existData(String key) {
        return Boolean.TRUE.equals(template.hasKey(key));
    }

    public void setDataExpire(String key, String value, long duration) {
        try {
            log.info("[setDataExpire] key: {}, value: {}", key, value);
            ValueOperations<String, String> valueOperations = template.opsForValue();
            Duration expireDuration = Duration.ofMinutes(duration);
            valueOperations.set(key, value, expireDuration);
        } catch (Exception e) {
            // 로그를 추가하여 예외를 기록합니다.
            System.err.println("Error saving to Redis: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteData(String key) {
        template.delete(key);
    }
}