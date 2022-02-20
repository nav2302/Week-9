package com.greatlearning.week8assignment.Restraunt.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greatlearning.week8assignment.Restraunt.model.Order;
import com.greatlearning.week8assignment.Restraunt.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	OrderRepository orderRepository;

	@Override
	public Iterable<Order> getAllOrders() {
		// TODO Auto-generated method stub
		return this.orderRepository.findAll();
	}

	@Override
	public Order create(Order order) {
		// TODO Auto-generated method stub
		order.setDateCreated(LocalDate.now());
        return this.orderRepository.save(order);
	}

	@Override
	public void update(Order order) {
		// TODO Auto-generated method stub
		this.orderRepository.save(order);
	}

	@Override
	public List<Order> findAllByDateCreated(LocalDate date) {
		// TODO Auto-generated method stub
		return orderRepository.findAllByDateCreated(date);
	}

	@Override
	public List<Order> findOrdersForThisMonth(LocalDate start, LocalDate end) {
		return orderRepository.findByDateCreatedGreaterThanAndDateCreatedLessThan(start,end);
	}

}
