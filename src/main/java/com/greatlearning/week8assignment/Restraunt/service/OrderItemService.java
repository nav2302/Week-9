package com.greatlearning.week8assignment.Restraunt.service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.greatlearning.week8assignment.Restraunt.model.OrderItem;

@Validated
public interface OrderItemService {

    OrderItem create(@NotNull(message = "The Items for order cannot be null.") @Valid OrderItem orderItem);
}