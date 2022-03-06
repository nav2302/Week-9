package com.greatlearning.week8assignment.Restraunt.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.greatlearning.week8assignment.Restraunt.RestrauntApplication;
import com.greatlearning.week8assignment.Restraunt.model.UserDto;
import com.greatlearning.week8assignment.Restraunt.response.UserResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { RestrauntApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegisterControllerTest {
	
	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	@Autowired
	RegisterController resgisterController;

	@Test
	void testRegisterUser() {
		ResponseEntity<UserResponse> postResponse = restTemplate
				.postForEntity(getRegisterControllerApiName(), prepareUserForm(), UserResponse.class);
				
		UserResponse registeredUser = postResponse.getBody();
		assertThat(registeredUser).isNotNull();
		assertThat(registeredUser.getFirstName()).isEqualTo("testRegisterUserFirstName");
		assertThat(registeredUser.getEmail()).isEqualTo("register_test@gmail.com");
		
		//Negative case trying to save same user again
		ResponseEntity<UserResponse> errorResponse = restTemplate
				.postForEntity(getRegisterControllerApiName(), prepareUserForm(), UserResponse.class);
		assertThat(errorResponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
		
	}

	private String getRegisterControllerApiName() {
		return "http://localhost:" + port + "/api/register/";
	}
	
	private UserDto prepareUserForm() {
		return UserDto.builder().firstName("testRegisterUserFirstName").lastName("testRegisterUserLasName").email("register_test@gmail.com")
				.password("test").build();
	}

}
