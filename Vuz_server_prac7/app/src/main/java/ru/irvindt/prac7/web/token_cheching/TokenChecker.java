package ru.irvindt.prac7.web.token_cheching;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.irvindt.prac7.redis.RedisLoader;
import ru.irvindt.prac7.db.model.UserRole;
import ru.irvindt.prac7.db.redis.TokenInformation;

@Component
public class TokenChecker {
	private final RedisLoader redisLoader;

	@Autowired
	public TokenChecker(RedisLoader redisLoader) {
		this.redisLoader = redisLoader;
	}

	/**
	 * @param header - token from request, probably in headers
	 * @return - user role, if token is valid, null - if token is invalid
	 */
	public UserRole getRoleFromTokenHeader(String header) {
		if(header == null) {
			return null;
		}
		TokenInformation token = redisLoader.getTokenInformation(header.replace("Bearer ", ""));

		if (token == null) {
			return null;
		}
		return UserRole.valueOf(token.getRole());
	}
}
