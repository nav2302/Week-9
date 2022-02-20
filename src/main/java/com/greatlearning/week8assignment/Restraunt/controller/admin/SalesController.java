package com.greatlearning.week8assignment.Restraunt.controller.admin;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greatlearning.week8assignment.Restraunt.config.SwaggerConfig;
import com.greatlearning.week8assignment.Restraunt.model.Item;
import com.greatlearning.week8assignment.Restraunt.model.Order;
import com.greatlearning.week8assignment.Restraunt.model.OrderBillWrapper;
import com.greatlearning.week8assignment.Restraunt.response.ItemResponse;
import com.greatlearning.week8assignment.Restraunt.service.OrderService;

import io.swagger.annotations.Api;


@Api(tags = { SwaggerConfig.SalesController_TAG })
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")// Authorizing only Admin
public class SalesController {

	@Autowired
	OrderService orderService;

	@GetMapping(value = "/bills")
	public ResponseEntity<OrderBillWrapper> getAllOrdersForToday() {

		List<Order> orderList = Optional.ofNullable(orderService.findAllByDateCreated(LocalDate.now())).orElse(null);
		Double totalBill = orderList.stream().mapToDouble(Order::getTotalOrderPrice).sum();

		List<ItemResponse> items = new ArrayList<>();
		orderList.stream().forEach(order -> order.getOrderItems().forEach(orderItem -> {
			Item item = orderItem.getItem();
			items.add(ItemResponse.builder().name(item.getName()).price(item.getPrice())
					.quantity(orderItem.getQuantity()).build());
		}));
		return new ResponseEntity<>(OrderBillWrapper.builder().bill(totalBill).items(items).build(), HttpStatus.OK);
	}

	@GetMapping(value = "/sales")
	public ResponseEntity<String> getSalesForThisMonth() {
		LocalDate start = LocalDate.ofEpochDay(System.currentTimeMillis() / (24 * 60 * 60 * 1000)).withDayOfMonth(1);

		LocalDate end = LocalDate.ofEpochDay(System.currentTimeMillis() / (24 * 60 * 60 * 1000)).plusMonths(1)
				.withDayOfMonth(1).minusDays(1);
		List<Order> orderList = Optional.ofNullable(orderService.findOrdersForThisMonth(start, end)).orElse(null);

		Double totalSales = orderList.stream().mapToDouble(Order::getTotalOrderPrice).sum();
		return new ResponseEntity<>(new String("Total sales for this month " + totalSales), HttpStatus.OK);
	}
}
