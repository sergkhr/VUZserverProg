package ru.irvindt.prac7.db.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisSaver {
	@Value("${app.expirationTimeInMs}")
	private int jwtExpirationTime;

	private final RedisTemplate<String, Object> redisTemplate;

	@Autowired
	public RedisSaver(RedisConfig config) {
		this.redisTemplate = config.redisTemplate();
	}

	public void saveTokenInformation(String key, TokenInformation person) {
		redisTemplate.opsForValue().set(key, person, jwtExpirationTime, TimeUnit.MILLISECONDS);
	}

	public TokenInformation getTokenInformation(String key) {
		return (TokenInformation) redisTemplate.opsForValue().get(key);
	}

	public void deleteTokenInformation(String key) {
		redisTemplate.delete(key);
	}
}
