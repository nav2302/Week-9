package com.greatlearning.week8assignment.Restraunt.model;

import java.beans.Transient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "orders")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "orderItems")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id", nullable = false)
	private Long id;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate dateCreated;

	private String status;

	@OneToMany(mappedBy = "pk.order")
	@Valid
	private List<OrderItem> orderItems = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	public Order() {
	}

	public Order(LocalDate dateCreated, String status) {
		super();
		this.dateCreated = dateCreated;
		this.status = status;
	}

	// Using transient to ignore this field since it is calculated and we don't want
	// it to be saved in DB unnecessarily
	@Transient
	public Double getTotalOrderPrice() {
		double sum = 0D;
		List<OrderItem> orderProducts = getOrderItems();
		for (OrderItem op : orderProducts) {
			sum += op.getTotalPrice();
		}
		return sum;
	}

	@Transient
	public int getNumberOfItems() {
		return this.orderItems.size();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Order))
			return false;
		return id != null && id.equals(((Order) o).getId());
	}

	@Override
	public int hashCode() {
		return 31;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDate dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
