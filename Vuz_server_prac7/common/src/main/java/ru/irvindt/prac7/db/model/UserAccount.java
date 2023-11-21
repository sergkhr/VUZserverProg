package ru.irvindt.prac7.db.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserAccount {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	@Setter
	private Long id;

	@Getter
	@Setter
	private String login;

	@Getter
	@Setter
	private String password;

	@Enumerated(EnumType.STRING)
	@Getter
	@Setter
	private UserRole role = UserRole.CLIENT;
}
