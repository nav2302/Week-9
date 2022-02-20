package com.greatlearning.week8assignment.Restraunt.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.greatlearning.week8assignment.Restraunt.config.SwaggerConfig;
import com.greatlearning.week8assignment.Restraunt.exception.ItemNotFoundException;
import com.greatlearning.week8assignment.Restraunt.model.Item;
import com.greatlearning.week8assignment.Restraunt.model.Order;
import com.greatlearning.week8assignment.Restraunt.model.OrderBillWrapper;
import com.greatlearning.week8assignment.Restraunt.model.OrderItem;
import com.greatlearning.week8assignment.Restraunt.model.OrderItemDto;
import com.greatlearning.week8assignment.Restraunt.model.User;
import com.greatlearning.week8assignment.Restraunt.response.ItemResponse;
import com.greatlearning.week8assignment.Restraunt.service.ItemService;
import com.greatlearning.week8assignment.Restraunt.service.OrderItemService;
import com.greatlearning.week8assignment.Restraunt.service.OrderService;
import com.greatlearning.week8assignment.Restraunt.service.UserService;

import io.swagger.annotations.Api;

@Api(tags = { SwaggerConfig.OrderController_TAG })
@RestController
@RequestMapping("/api/orders")
public class OrderController {

	@Autowired
	OrderService orderService;

	@Autowired
	OrderItemService orderItemService;

	@Autowired
	ItemService itemService;

	@Autowired
	UserService userService;

	@PostMapping
	@Transactional
	public ResponseEntity<OrderBillWrapper> sendOrderForSelectedItems(@RequestBody OrderForm form) {
		List<OrderItemDto> formDtos = form.getItemOrders();

		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findByEmail(auth.getName());

		validateProductsExistence(formDtos);
		Order order = new Order();
		user.addOrder(order);
		order = this.orderService.create(order);

		List<OrderItem> orderItems = new ArrayList<>();
		for (OrderItemDto dto : formDtos) {
			orderItems.add(orderItemService
					.create(new OrderItem(order, itemService.findItemById(dto.getItem().getId()), dto.getQuantity())));
		}
		order.setOrderItems(orderItems);
		this.orderService.update(order);

		String uri = ServletUriComponentsBuilder.fromCurrentServletMapping().path("/orders/{id}")
				.buildAndExpand(order.getId()).toString();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", uri);
		
		
		List<ItemResponse> items = new ArrayList<>();
		order.getOrderItems().stream().forEach(orderItem -> {
			Item item = orderItem.getItem();
			items.add(ItemResponse.builder().name(item.getName()).price(item.getPrice()).quantity(orderItem.getQuantity()).build());
		});

		return new ResponseEntity<>(OrderBillWrapper.builder().bill(order.getTotalOrderPrice()).items(items).build(), headers, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<OrderBillWrapper> getAllOrdersForCurrentLoggedInUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findByEmail(auth.getName());
		List<Order> orderList = Optional.ofNullable(user.getOrders()).orElse(null);
		Double totalBill = orderList.stream().mapToDouble(Order::getTotalOrderPrice).sum();

		List<ItemResponse> items = new ArrayList<>();
		orderList.stream().forEach(order -> order.getOrderItems().forEach(orderItem -> {
			Item item = orderItem.getItem();
			items.add(ItemResponse.builder().name(item.getName()).price(item.getPrice()).quantity(orderItem.getQuantity()).build());
		}));
		return new ResponseEntity<>(OrderBillWrapper.builder().bill(totalBill).items(items).build(), HttpStatus.OK);
	}

	public static class OrderForm {

		private List<OrderItemDto> itemOrders;

		public List<OrderItemDto> getItemOrders() {
			return itemOrders;
		}

		public void setItemOrders(List<OrderItemDto> itemOrders) {
			this.itemOrders = itemOrders;
		}
	}

	private void validateProductsExistence(List<OrderItemDto> orderProducts) {
		List<OrderItemDto> list = orderProducts.stream()
				.filter(op -> Objects.isNull(itemService.findItemById(op.getItem().getId())))
				.collect(Collectors.toList());

		if (!CollectionUtils.isEmpty(list)) {
			new ItemNotFoundException("Item not found");
		}
	}
}
