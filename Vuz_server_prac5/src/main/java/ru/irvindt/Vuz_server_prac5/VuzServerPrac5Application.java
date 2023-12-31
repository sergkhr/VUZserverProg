package ru.irvindt.Vuz_server_prac5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@ComponentScan(basePackages = "ru.irvindt.Vuz_server_prac5.*")
//@EntityScan("ru.irvindt.Vuz_server_prac5.db.model")
@EnableJpaRepositories("ru.irvindt.Vuz_server_prac5.*")
public class VuzServerPrac5Application {

	public static void main(String[] args) {
		SpringApplication.run(VuzServerPrac5Application.class, args);
	}

}
