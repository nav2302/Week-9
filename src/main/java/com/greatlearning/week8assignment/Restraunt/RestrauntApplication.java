package com.greatlearning.week8assignment.Restraunt;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.greatlearning.week8assignment.Restraunt.exception.MailAlreadyExistsException;
import com.greatlearning.week8assignment.Restraunt.model.Role;
import com.greatlearning.week8assignment.Restraunt.model.User;
import com.greatlearning.week8assignment.Restraunt.service.UserService;

import lombok.extern.slf4j.Slf4j;

/*
 * PLEASE NOTE THE SAMPLE USER ID AND PASSWORD FOR ADMIN AND USER HAVE BEEN DEFINED BELOW YOU CAN USE THAT TO AUTHENTICATE IN ORDER 
 * TO USE SWAGGER APIS
 */
@SpringBootApplication
@Slf4j
public class RestrauntApplication implements CommandLineRunner {

	@Autowired
	UserService userService;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		SpringApplication.run(RestrauntApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Creating one sample User and Admin User

		User adminUser = User.builder().email("admin@gmail.com").firstName("admin").lastName("last")
				.password(bCryptPasswordEncoder.encode("admin"))
				.roles(Arrays.asList(Role.builder().name("ROLE_ADMIN").build())).build();
		User sampleUser = User.builder().email("user@gmail.com").firstName("user").lastName("last")
				.password(bCryptPasswordEncoder.encode("user"))
				.roles(Arrays.asList(Role.builder().name("ROLE_USER").build())).build();
		
		
		try {
			userService.save(adminUser);
			userService.save(sampleUser);
		} catch (MailAlreadyExistsException e) {
			log.error("Mail Already exists in Database");
		}
	}

}
