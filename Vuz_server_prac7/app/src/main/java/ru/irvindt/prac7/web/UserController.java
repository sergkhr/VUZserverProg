package ru.irvindt.prac7.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.irvindt.prac7.redis.RedisLoader;
import ru.irvindt.prac7.web.token_cheching.TokenChecker;
import ru.irvindt.prac7.db.model.UserAccount;
import ru.irvindt.prac7.db.model.UserRole;
import ru.irvindt.prac7.db.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private final UserRepository userRepository;
	private final RedisLoader redisLoader;
	private final TokenChecker tokenChecker;

	@Autowired
	public UserController(UserRepository userRepository,
	                      RedisLoader redisLoader, TokenChecker tokenChecker ) {
		this.userRepository = userRepository;
		this.redisLoader = redisLoader;
		this.tokenChecker = tokenChecker;
	}

	@GetMapping("/")
	public List<UserAccount> getAllClients() {
		return userRepository.findAll();
	}

	@GetMapping("/{id}")
	public UserAccount getClientById(@PathVariable Long id) {
		return userRepository.findById(id).orElse(null);
	}

	@PostMapping("/")
	public UserAccount createClient(@RequestBody UserAccount userAccount) {
		if (userRepository.findByLogin(userAccount.getLogin()).isEmpty()) {
			return userRepository.save(userAccount);
		}
		return null;
	}

	@PutMapping("/{id}")
	public UserAccount updateClient(@PathVariable Long id, @RequestBody UserAccount userAccount,
	                                @RequestHeader("Authorization") String header) {
		//check if userAccount is admin
		if (tokenChecker.getRoleFromTokenHeader(header) != UserRole.ADMIN) {
			UserAccount responceUserAccount = new UserAccount();
			responceUserAccount.setLogin("You are not admin");
			return responceUserAccount;
		}

		if (userRepository.existsById(id)) {
			userAccount.setId(id);
			return userRepository.save(userAccount);
		}
		return null;
	}

	@DeleteMapping("/{id}")
	public String deleteClient(@PathVariable Long id,
	                           @RequestHeader("Authorization") String header) {
		//check if user is admin
		if (tokenChecker.getRoleFromTokenHeader(header) != UserRole.ADMIN) {
			return "You are not admin";
		}

		userRepository.deleteById(id);
		return "UserAccount deleted";
	}

	@PutMapping("/{id}/becomeSeller")
	public String becomeSeller(@PathVariable Long id,
	                           @RequestHeader("Authorization") String header) {
		//check if userAccount is admin
		if (tokenChecker.getRoleFromTokenHeader(header) != UserRole.ADMIN) {
			return "You are not admin";
		}

		UserAccount userAccount = userRepository.getReferenceById(id);
		userAccount.setRole(UserRole.SELLER);
		userRepository.save(userAccount);
		redisLoader.deleteTokenInformation(header.replace("Bearer ", ""));
		return "UserAccount became seller";
	}

	@PutMapping("/{id}/becomeClient")
	public String becomeClient(@PathVariable Long id,
	                           @RequestHeader("Authorization") String header) {
		//check if userAccount is admin
		if (tokenChecker.getRoleFromTokenHeader(header) != UserRole.ADMIN) {
			return "You are not admin";
		}

		UserAccount userAccount = userRepository.getReferenceById(id);
		userAccount.setRole(UserRole.CLIENT);
		userRepository.save(userAccount);
		redisLoader.deleteTokenInformation(header.replace("Bearer ", ""));
		return "UserAccount became client";
	}

	@PutMapping("/{id}/becomeAdmin")
	public String becomeAdmin(@PathVariable Long id,
	                          @RequestHeader("Authorization") String header) {
		//check if userAccount is admin
		if (tokenChecker.getRoleFromTokenHeader(header) != UserRole.ADMIN) {
			return "You are not admin";
		}

		UserAccount userAccount = userRepository.getReferenceById(id);
		userAccount.setRole(UserRole.ADMIN);
		userRepository.save(userAccount);
		redisLoader.deleteTokenInformation(header.replace("Bearer ", ""));
		return "UserAccount became admin";
	}

}
