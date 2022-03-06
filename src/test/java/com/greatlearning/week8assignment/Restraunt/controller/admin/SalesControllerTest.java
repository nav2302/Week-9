package com.greatlearning.week8assignment.Restraunt.controller.admin;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.greatlearning.week8assignment.Restraunt.RestrauntApplication;
import com.greatlearning.week8assignment.Restraunt.model.OrderBillWrapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { RestrauntApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SalesControllerTest {
	
	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;
	
	@Autowired
	SalesController salesController;

	@Test
	void shouldGetAllBillsForToday() {
		ResponseEntity<OrderBillWrapper> responseEntity = restTemplate.withBasicAuth("admin@gmail.com", "admin")
				.exchange(getSalesControllerApiName() + "bills", HttpMethod.GET, null, OrderBillWrapper.class);
		OrderBillWrapper responseBill = responseEntity.getBody();
		
		assertThat(responseBill.getBill()).isEqualTo(400);
	}

	@Test
	void shouldGetSalesForThisMonth() {
		ResponseEntity<String> responseEntity = restTemplate.withBasicAuth("admin@gmail.com", "admin")
				.exchange(getSalesControllerApiName() + "sales", HttpMethod.GET, null, String.class);
		System.out.println(responseEntity);
		assertThat(responseEntity.getBody()).contains("Total sales");
		assertThat(responseEntity.getBody()).contains("400");
	}
	

	private String getSalesControllerApiName() {
		return "http://localhost:" + port + "/api/admin/";
	}

}
