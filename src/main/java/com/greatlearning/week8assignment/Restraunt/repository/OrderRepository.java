package com.greatlearning.week8assignment.Restraunt.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.greatlearning.week8assignment.Restraunt.model.Order;
import com.greatlearning.week8assignment.Restraunt.model.OrderItemPK;

@Repository
public interface OrderRepository extends JpaRepository<Order, OrderItemPK> {

	List<Order> findAllByDateCreated(LocalDate date);
	List<Order> findByDateCreatedGreaterThanAndDateCreatedLessThan(LocalDate start, LocalDate end);
}