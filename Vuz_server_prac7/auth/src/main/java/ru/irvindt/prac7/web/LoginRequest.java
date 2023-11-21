package ru.irvindt.prac7.web;

import lombok.Data;

@Data
public class LoginRequest {
	private String username;
	private String password;
}
