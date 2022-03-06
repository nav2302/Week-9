package com.greatlearning.week8assignment.Restraunt.controller.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestClientException;

import com.greatlearning.week8assignment.Restraunt.RestrauntApplication;
import com.greatlearning.week8assignment.Restraunt.model.UserDto;
import com.greatlearning.week8assignment.Restraunt.response.UserResponse;


/*
 * Make Sure you run the CREATE/UPDATE user before Delete user
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { RestrauntApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	@Test
	void shouldGetAllUsers() {
		ResponseEntity<Iterable<UserResponse>> responseEntity = restTemplate.withBasicAuth("admin@gmail.com", "admin")
				.exchange(getAllUsersApiName(), HttpMethod.GET, null,
						new ParameterizedTypeReference<Iterable<UserResponse>>() {
						});
		Iterable<UserResponse> allResponseUsers = responseEntity.getBody();

		// Positive Test case
		assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
		assertThat(allResponseUsers).isNotEmpty().hasSize(2);
		assertThat(allResponseUsers).filteredOn("email", "admin@gmail.com").isNotEmpty();
		assertThat(allResponseUsers).filteredOn("email", "user@gmail.com").isNotEmpty();

		// Negative Test case
		assertThatThrownBy(() -> {
			restTemplate.withBasicAuth("user@gmail.com", "user").exchange(getAllUsersApiName(), HttpMethod.GET, null,
					new ParameterizedTypeReference<Iterable<UserResponse>>() {
					});
		}).isInstanceOf(RestClientException.class).hasMessageContaining("Error while extracting response");
	}

	@Test
	void shouldGetUserByEmail() {
		ResponseEntity<UserResponse> responseEntity = restTemplate.withBasicAuth("admin@gmail.com", "admin")
				.exchange(getAllUsersApiName() + "user@gmail.com", HttpMethod.GET, null, UserResponse.class);
		UserResponse responseUser = responseEntity.getBody();

		// Positive Test case
		assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
		assertThat(responseUser).isNotNull();
		assertThat(responseUser).hasNoNullFieldsOrProperties();
		assertThat(responseUser.getFirstName()).isEqualTo("user");
		assertThat(responseUser.getLastName()).isEqualTo("last");

		// Negative Test case
		ResponseEntity<UserResponse> emptyUserResponseEntity = restTemplate.withBasicAuth("admin@gmail.com", "admin")
				.exchange(getAllUsersApiName() + "random@gmail.com", HttpMethod.GET, null, UserResponse.class);

		UserResponse shouldBeEmptyUserResponse = emptyUserResponseEntity.getBody();
		assertThat(emptyUserResponseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.CONFLICT);
		assertThat(shouldBeEmptyUserResponse).hasAllNullFieldsOrProperties();

	}

	@Test
	void shouldSaveAUser() {
		ResponseEntity<UserResponse> postResponse = restTemplate.withBasicAuth("admin@gmail.com", "admin")
				.postForEntity(getAllUsersApiName(), prepareUserForm(), UserResponse.class);
		UserResponse responseOrder = postResponse.getBody();
		assertThat(responseOrder).isNotNull();
		assertThat(responseOrder.getFirstName()).isEqualTo("testUserFirstName");
		assertThat(responseOrder.getEmail()).isEqualTo("test@gmail.com");

		// Negative Test Cases, adding same user again
		ResponseEntity<UserResponse> negativePostResponse = restTemplate.withBasicAuth("admin@gmail.com", "admin")
				.postForEntity(getAllUsersApiName(), prepareUserForm(), UserResponse.class);

		UserResponse shouldBeEmptyUserResponse = negativePostResponse.getBody();
		assertThat(negativePostResponse.getStatusCode()).isEqualByComparingTo(HttpStatus.CONFLICT);
		assertThat(shouldBeEmptyUserResponse).hasAllNullFieldsOrProperties();
	}

	@Test
	void shouldUpdateAUser() {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<UserDto> requestUpdate = new HttpEntity<>(prepareUpdateUserForm(), headers);
		ResponseEntity<UserResponse> putResponse = restTemplate.withBasicAuth("admin@gmail.com", "admin")
				.exchange(getAllUsersApiName() + "test@gmail.com", HttpMethod.PUT, requestUpdate, UserResponse.class);
		UserResponse putUserResponse = putResponse.getBody();

		assertThat(putResponse.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
		assertThat(putUserResponse.getFirstName()).isEqualTo("testUserFirstNameUpdated");
		assertThat(putUserResponse.getLastName()).isEqualTo("testUserLasNameUpdated");

	}
	
	@Test
	void shouldDeleteAUser() {
		ResponseEntity<UserResponse> deleteUserResponse = restTemplate.withBasicAuth("admin@gmail.com", "admin")
				.exchange(getAllUsersApiName() + "test@gmail.com", HttpMethod.DELETE, null, UserResponse.class);
		
		assertThat(deleteUserResponse.getStatusCode()).isEqualByComparingTo(HttpStatus.NO_CONTENT);
	}

	private String getAllUsersApiName() {
		return "http://localhost:" + port + "/api/admin/users/";
	}

	private UserDto prepareUserForm() {
		return UserDto.builder().firstName("testUserFirstName").lastName("testUserLasName").email("test@gmail.com")
				.password("test").build();
	}

	private UserDto prepareUpdateUserForm() {
		return UserDto.builder().firstName("testUserFirstNameUpdated").lastName("testUserLasNameUpdated")
				.email("test@gmail.com").password("test").build();
	}

}
