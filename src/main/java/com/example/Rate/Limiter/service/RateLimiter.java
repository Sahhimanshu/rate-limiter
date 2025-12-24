package com.example.Rate.Limiter.service;

public interface RateLimiter {
    boolean isAllowed(String key, String resource);
}
