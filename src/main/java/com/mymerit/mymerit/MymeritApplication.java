package com.mymerit.mymerit;

import com.mymerit.mymerit.domain.entity.User;
import com.mymerit.mymerit.domain.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MymeritApplication {
	public static void main(String[] args) {
		SpringApplication.run(MymeritApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(UserRepository userRepository) {
		return args -> {
			User user = new User();

			userRepository.insert(user);
		};
	}
}
