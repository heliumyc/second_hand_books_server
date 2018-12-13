package com.bookexchange.app.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.HttpHeaders;
import java.util.concurrent.TimeUnit;

@Component
public class AuthValidationHelper {
    static final int TOKEN_EXPIRE_TIME = 60*60*6;

    private final RedisTemplate<String, Integer> redisTemplate;

    @Autowired
    public AuthValidationHelper(RedisTemplate<String, Integer> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Integer checkSessionId(String sessionId) {
        if (sessionId == null) return null;
        Integer uid = redisTemplate.opsForValue().get(sessionId);
        return uid;
    }

    public void storeSessionId(String sessionId, int uid) {
        redisTemplate.opsForValue().set(sessionId, uid, TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
    }

    public String getAuthorizationCode(String authHeader) {
        if (authHeader == null) return null;
        // "Bearer XXXXXXXX" starts from 7
        if (authHeader.length() < 7) return null; // cannot contain "Bearer "
        return authHeader.substring(7);
    }

    public Integer validate(String authHeader) {
        return checkSessionId(getAuthorizationCode(authHeader));
    }

}
