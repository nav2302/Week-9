package com.greatlearning.week8assignment.Restraunt.controller;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.greatlearning.week8assignment.Restraunt.config.SwaggerConfig;
import com.greatlearning.week8assignment.Restraunt.model.Item;
import com.greatlearning.week8assignment.Restraunt.service.ItemServiceImpl;
import com.greatlearning.week8assignment.Restraunt.service.UserService;

import io.swagger.annotations.Api;

@Api(tags = { SwaggerConfig.ItemController_TAG })
@RestController
@RequestMapping("/api/items")
public class ItemController {

	@Autowired
	ItemServiceImpl itemService;

	@Autowired
	UserService userService;

	@GetMapping(value = { "", "/" })
	@ResponseStatus(HttpStatus.OK)
	public @NotNull ResponseEntity<Iterable<Item>> getAllItems() {

//		Item item = Item.builder().name("Item2").price(100.0).build();
//		itemService.save(item);
		return new ResponseEntity<>(itemService.getAllItems(), HttpStatus.OK) ;
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable("id") @Min(1) Long id) {
		return ResponseEntity.ok().body(itemService.findItemById(id));
	}

}
