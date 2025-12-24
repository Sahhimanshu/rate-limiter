package com.example.Rate.Limiter.serviceImpl;

import com.example.Rate.Limiter.service.RateLimiter;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@AllArgsConstructor
@Service
public class RedisFixedWindowRateLimiter implements RateLimiter {

    private static final int MAX_REQUESTS = 5;
    private static final Duration WINDOW_SIZE = Duration.ofSeconds(60);
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean isAllowed(String key, String resource) {
        String redisKey = buildRedisKey(key, resource);
        Long currentCount = redisTemplate.opsForValue().increment(redisKey);
        if (currentCount == null) {
            return false;
        }
        if (currentCount == 1) {
            redisTemplate.expire(redisKey, WINDOW_SIZE);
        }
        return currentCount <= MAX_REQUESTS;
    }

    private String buildRedisKey(String key, String resource) {
        if (resource == null || resource.isEmpty()) {
            return "rate_limit:" + key;
        }
        return "rate_limit:" + resource + ":" + key;
    }
}
