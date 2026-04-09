package com.example.bp_spring_backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class LoginAttemptService {

    private final StringRedisTemplate redisTemplate;

    private static final int MAX_ATTEMPTS = 5;
    private static final int[] BLOCK_SECONDS = {30, 180, 300, 900, 1800};
    private static final Duration RECORD_TTL = Duration.ofMinutes(30);

    private String buildKey(String key) {
        return "login:" + key;
    }

    public void loginFailed(String key) {
        String redisKey = buildKey(key);

        Long attempts = redisTemplate.opsForHash().increment(redisKey, "attempts", 1);
        long now = System.currentTimeMillis() / 1000;

        if (attempts % MAX_ATTEMPTS == 0) {
            int index = Math.min((attempts.intValue() / MAX_ATTEMPTS) - 1, BLOCK_SECONDS.length - 1);
            int blockTime = BLOCK_SECONDS[index];
            long blockedUntil = now + blockTime;

            redisTemplate.opsForHash().put(redisKey, "blocked_until", String.valueOf(blockedUntil));
        }

        redisTemplate.expire(redisKey, RECORD_TTL);
    }

    public void loginSucceeded(String key) {
        redisTemplate.delete(buildKey(key));
    }

    public boolean isBlocked(String key) {
        String redisKey = buildKey(key);
        Object blockedUntilStr = redisTemplate.opsForHash().get(redisKey, "blocked_until");
        if (blockedUntilStr == null) return false;

        long blockedUntil = Long.parseLong(blockedUntilStr.toString());
        long now = System.currentTimeMillis() / 1000;

        return now < blockedUntil;
    }

    public long getRemainingBlockSeconds(String key) {
        String redisKey = buildKey(key);
        Object blockedUntilStr = redisTemplate.opsForHash().get(redisKey, "blocked_until");
        if (blockedUntilStr == null) return 0;

        long blockedUntil = Long.parseLong(blockedUntilStr.toString());
        long now = System.currentTimeMillis() / 1000;

        return Math.max(0, blockedUntil - now);
    }
}
