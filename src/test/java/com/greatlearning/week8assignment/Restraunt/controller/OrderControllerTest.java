package com.greatlearning.week8assignment.Restraunt.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.greatlearning.week8assignment.Restraunt.RestrauntApplication;
import com.greatlearning.week8assignment.Restraunt.controller.OrderController.OrderForm;
import com.greatlearning.week8assignment.Restraunt.model.Item;
import com.greatlearning.week8assignment.Restraunt.model.OrderBillWrapper;
import com.greatlearning.week8assignment.Restraunt.model.OrderItemDto;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { RestrauntApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	@Autowired
	OrderController orderController;

	@Test
	void shouldShowSelectedItemsAndBill() {
		ResponseEntity<OrderBillWrapper> postResponse = restTemplate.withBasicAuth("user@gmail.com", "user")
				.postForEntity(getOrderControllerApiName(), prepareOrderForm(), OrderBillWrapper.class);
		OrderBillWrapper responseOrder = postResponse.getBody();
		
		//Positive Test cases
		assertThat(postResponse.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
		assertThat(responseOrder.getBill()).isEqualByComparingTo(200.00);
		assertThat(responseOrder.getItems()).filteredOn("name", "Item2").isNotEmpty();

		// Negative Test Cases
		ResponseEntity<OrderBillWrapper> negativePostResponseTest = restTemplate.withBasicAuth("user@gmail.com", "user")
				.postForEntity(getOrderControllerApiName(), prepareNegativeTestOrderForm(), OrderBillWrapper.class);
		OrderBillWrapper negativeResponseOrder = negativePostResponseTest.getBody();
		
		assertThat(negativePostResponseTest.getStatusCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
		assertThat(negativeResponseOrder).usingRecursiveComparison()
				.isEqualTo(OrderBillWrapper.builder().items(null).bill(null).build());

	}

	@Test
	void shouldGenerateBillForAParticularUser() {
		ResponseEntity<OrderBillWrapper> responseEntity = restTemplate.withBasicAuth("user@gmail.com", "user")
				.exchange(getOrderControllerApiName(), HttpMethod.GET, null, OrderBillWrapper.class);
		OrderBillWrapper responseOrder = responseEntity.getBody();

		assertThat(responseOrder).isNotNull();
		assertThat(responseOrder.getItems()).isNotEmpty().isNotNull().hasSize(2);
		assertThat(responseOrder.getBill()).isEqualByComparingTo(400.00);
	}

	private String getOrderControllerApiName() {
		return "http://localhost:" + port + "/api/orders/";
	}

	private OrderForm prepareOrderForm() {
		OrderForm orderForm = new OrderForm();
		orderForm.setItemOrders(Collections.singletonList(OrderItemDto.builder()
				.item(Item.builder().id(1l).name("Item2").price(100.00).build()).quantity(2).build()));
		return orderForm;
	}

	private OrderForm prepareNegativeTestOrderForm() {

		// Item 7 does not exists in DB
		OrderForm orderForm = new OrderForm();
		orderForm.setItemOrders(Collections.singletonList(OrderItemDto.builder()
				.item(Item.builder().id(7l).name("Item7").price(100.00).build()).quantity(2).build()));
		return orderForm;
	}

}
