package ru.irvindt.prac7;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
//@ComponentScan(basePackages = "ru.irvindt.Vuz_server_prac5.*")
//@EntityScan("ru.irvindt.Vuz_server_prac5.db.model")
@EnableJpaRepositories("ru.irvindt.prac7.*")
@EnableRedisRepositories("ru.irvindt.prac7.db.redis")
public class VuzServerPrac5Application {

	public static void main(String[] args) {
		SpringApplication.run(VuzServerPrac5Application.class, args);
	}

}
