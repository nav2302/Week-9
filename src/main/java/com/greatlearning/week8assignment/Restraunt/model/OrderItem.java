package com.greatlearning.week8assignment.Restraunt.model;

import java.beans.Transient;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class OrderItem {

	@EmbeddedId
	@JsonIgnore
	private OrderItemPK pk;

	@Column(nullable = false)
	private Integer quantity;

	public OrderItem(Order order, Item item, Integer quantity) {
		pk = new OrderItemPK();
		pk.setOrder(order);
		pk.setItem(item);
		this.quantity = quantity;
	}

	@Transient
	public Item getItem() {
		return this.pk.getItem();
	}

	@Transient
	public Double getTotalPrice() {
		return getItem().getPrice() * getQuantity();
	}
}