package com.greatlearning.week8assignment.Restraunt.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.greatlearning.week8assignment.Restraunt.RestrauntApplication;
import com.greatlearning.week8assignment.Restraunt.model.Item;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { RestrauntApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	@Autowired
	ItemController itemController;

	@Test
	public void testContextLoads() {
		assertThat(itemController).isNotNull();
	}

	@Test
	public void shouldGetAllItems() {

		// Get Items API call using testRestTemplate provided by spring framework
		ResponseEntity<Iterable<Item>> responseEntity = restTemplate.withBasicAuth("user@gmail.com", "user")
				.exchange(getAllItemsApiName(), HttpMethod.GET, null, new ParameterizedTypeReference<Iterable<Item>>() {
				});
		Iterable<Item> items = responseEntity.getBody();

		// Positive Test case
		assertThat(items).hasSize(3);
		assertThat(items).filteredOn("name", "Item2").isNotEmpty();

		// Negative Test Case
		assertThat(items).filteredOn("name", "Item3").isEmpty();
	}

	@Test
	public void shouldGetSpecifiedItem() {
		Long testItemId = 1l;
		Long testItemIdThatDoesNotExists = 6l;
		ResponseEntity<Item> positiveResponseEntity = restTemplate.withBasicAuth("user@gmail.com", "user")
				.exchange(getAllItemsApiName() + testItemId, HttpMethod.GET, null, Item.class);
		ResponseEntity<Item> negativeResponseEntity = restTemplate.withBasicAuth("user@gmail.com", "user")
				.exchange(getAllItemsApiName() + testItemIdThatDoesNotExists, HttpMethod.GET, null, Item.class);

		Item itemWithId = positiveResponseEntity.getBody();

		// Positive Test Cases
		assertThat(itemWithId).isNotNull();
		assertThat(itemWithId.getName()).isEqualTo("Item2");
		assertThat(itemWithId.getPrice()).isEqualTo(100);

		// Negative Test cases
		Item expectedNullItem = new Item(null, null, null);
		Item responseNullItem = negativeResponseEntity.getBody();

		assertThat(responseNullItem).usingRecursiveComparison().isEqualTo(expectedNullItem);

	}

	private String getAllItemsApiName() {
		return "http://localhost:" + port + "/api/items/";
	}

}
