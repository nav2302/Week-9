package com.greatlearning.week8assignment.Restraunt.config;

import java.util.Date;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.greatlearning.week8assignment.Restraunt.controller.OrderController.OrderForm;
import com.greatlearning.week8assignment.Restraunt.model.AuditLog;
import com.greatlearning.week8assignment.Restraunt.repository.AuditRepository;

@Configuration
@Aspect
public class AspectConfig {
	
	@Autowired
	AuditRepository auditRepository;
	
	@AfterReturning("execution(public * com.greatlearning.week8assignment.Restraunt.controller.OrderController.generateBill(..) )")
	public void logBeforeGenrateBill(JoinPoint joinPoint) {
		
		OrderForm orderForm = (OrderForm) joinPoint.getArgs()[0];
		auditRepository.save(AuditLog.builder().createDate(new Date()).description("Bill Generated " + orderForm.getItemOrders()).build());
	}

}
