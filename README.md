Rate Limiter Service (Spring Boot + Redis)
Overview

This project implements a rate limiter service that controls how frequently a client (user/IP/API key) can make requests within a given time window.

The service is designed to be:

Stateless

Lightweight

Suitable for distributed environments

It uses Redis as a shared, in-memory store to track request counts and enforce limits consistently across multiple application instances.

Why Rate Limiting?

Rate limiting is a critical component in backend systems to:

Protect services from overload

Prevent abuse and brute-force attacks

Ensure fair usage among clients

Control infrastructure and API costs

This service is intended to sit in front of application logic and make a fast allow/deny decision for each request.

Design Goals

Keep the service simple and focused

Avoid unnecessary persistence or databases

Rely on Redis for fast, ephemeral state

Be easy to reason about and extend

Architecture
Client / Application
        ↓
Rate Limiter Service
        ↓
      Redis


The service exposes an HTTP API to check whether a request is allowed.

Redis acts as the single source of truth for request counters.

The service itself remains stateless and horizontally scalable.

Algorithm Used
Fixed Window Rate Limiting

Rule:

Allow N requests per T seconds per identifier.

How it works conceptually:

Each client is identified by a unique key (e.g., user ID or IP).

A counter is maintained in Redis for the current time window.

If the counter exceeds the configured limit, further requests are rejected.

Redis key expiration automatically resets the counter when the window ends.

Why Fixed Window?

Simple to implement and explain

Efficient for high-throughput systems

Sufficient to demonstrate rate-limiting fundamentals

Redis Usage

Redis is used for temporary, short-lived state

Each rate-limit key has a TTL equal to the window duration

No permanent data is stored

Example key pattern:

rate_limit:{identifier}


Redis data loss or restart is considered acceptable, as rate-limit state is ephemeral.

Configuration

The service connects to Redis using Spring Boot auto-configuration with the Lettuce client.

Local environment: Redis without SSL

Production environment: Redis with SSL enabled

All environment differences are handled via configuration, not code changes.

Threading & Concurrency Model

No manual thread management is used

Spring Boot handles request threads

Redis atomic operations are relied upon for concurrency safety

Lettuce provides a thread-safe Redis client

This keeps the service simple and predictable under load.

Failure Handling

If Redis is unavailable, the service can be configured to:

Fail open (allow requests), or

Fail closed (block requests)

The choice depends on system requirements and is documented rather than hidden.

Limitations & Trade-offs

Fixed window algorithms can allow short bursts at window boundaries

Redis operations are not wrapped in Lua scripts in this version, which may allow rare race conditions under extreme concurrency

Advanced algorithms (token bucket, sliding window) are not implemented yet

These trade-offs are intentional to keep the initial design simple and clear.

Future Improvements

Token Bucket rate limiting

Sliding Window rate limiting

Redis Lua scripts for stronger atomicity

Metrics and monitoring

API-level or user-level dynamic policies

Tech Stack

Java

Spring Boot

Redis

Spring Data Redis (Lettuce client)

Summary

This project demonstrates:

Core backend system design principles

Proper use of Redis for distributed state

Clean separation of concerns

Avoidance of unnecessary complexity

It is intended as a learning and demonstration project, not a full API gateway.