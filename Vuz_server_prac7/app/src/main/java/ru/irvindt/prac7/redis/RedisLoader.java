package ru.irvindt.prac7.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.irvindt.prac7.db.redis.RedisConfig;
import ru.irvindt.prac7.db.redis.TokenInformation;

@Service
@Component
public class RedisLoader {
	private final RedisTemplate<String, Object> redisTemplate;

	@Autowired
	public RedisLoader(RedisConfig config) {
		this.redisTemplate = config.redisTemplate();
	}

	public TokenInformation getTokenInformation(String key) {
		return (TokenInformation) redisTemplate.opsForValue().get(key);
	}

	public void deleteTokenInformation(String key) {
		redisTemplate.delete(key);
	}
}
