package ru.irvindt.prac7.web;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.irvindt.prac7.db.redis.RedisSaver;
import ru.irvindt.prac7.db.redis.TokenInformation;
import ru.irvindt.prac7.db.repository.UserRepository;
import ru.irvindt.prac7.db.model.UserAccount;
import ru.irvindt.prac7.services.JwtTokenProvider;

import java.util.Objects;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepository;
	private final RedisSaver redisSaver;

	@Autowired
	public AuthController(JwtTokenProvider jwtTokenProvider, UserRepository userRepository, RedisSaver redisSaver) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.userRepository = userRepository;
		this.redisSaver = redisSaver;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		UserAccount userAccount = userRepository.findByLogin(loginRequest.getUsername()).orElse(null);
		if (userAccount == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("UserAccount not found");
		}
		if (Objects.equals(userAccount.getPassword(), loginRequest.getPassword())) {
			String token = jwtTokenProvider.generateToken(userAccount);
			redisSaver.saveTokenInformation(token, new TokenInformation(userAccount.getId(), userAccount.getRole().toString()));
			return ResponseEntity.ok(token);
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password");
	}

	@GetMapping("/validate")
	public ResponseEntity<?> validate(@RequestHeader("Authorization") String authorizationHeader) {
		if (jwtTokenProvider.validateToken(extractToken(authorizationHeader))) {
			Long id = jwtTokenProvider.getUserIdFromToken(extractToken(authorizationHeader));
			UserAccount userAccount = userRepository.findById(id).orElse(null);
			if (userAccount == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("UserAccount not found");
			}
			return ResponseEntity.ok(userAccount.getRole().toString());
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("There are no token");
	}

	@GetMapping("/check")
	public ResponseEntity<?> check(@RequestHeader("Authorization") String authorizationHeader) {
		if (jwtTokenProvider.validateToken(extractToken(authorizationHeader))) {
			TokenInformation token = redisSaver.getTokenInformation(extractToken(authorizationHeader));
			try {
				return ResponseEntity.ok(new Gson().toJson(token));
			} catch (NullPointerException e) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized");
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("There are no token");
	}

	private String extractToken(String authorizationHeader) {
		return authorizationHeader.replace("Bearer ", "");
	}
}
