package com.example.Rate.Limiter.controller;

import com.example.Rate.Limiter.dto.RateLimitRequest;
import com.example.Rate.Limiter.service.RateLimiter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class RateLimitingController {

    private final RateLimiter rateLimiter;

    @PostMapping(value = "/rate-limit/check")
    public ResponseEntity<Void> checkRateLimit(@RequestBody RateLimitRequest rateLimitRequest){
        boolean isAllowed = rateLimiter.isAllowed(rateLimitRequest.getKey(), rateLimitRequest.getResource());
        if (isAllowed){
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(null);
    }

}
