package com.greatlearning.week8assignment.Restraunt.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.greatlearning.week8assignment.Restraunt.model.OrderItem;
import com.greatlearning.week8assignment.Restraunt.repository.OrderItemRepository;

@Service
@Transactional
public class OrderItemServiceImpl implements OrderItemService {

	@Autowired
    private OrderItemRepository orderProductRepository;

    @Override
    public OrderItem create(OrderItem orderItem) {
        return this.orderProductRepository.save(orderItem);
    }
}
