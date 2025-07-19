package com.github.souqly.souqly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.souqly.souqly.model.Order;
import com.github.souqly.souqly.model.OrderStatus;
import com.github.souqly.souqly.payload.request.CreateOrderDTO;
import com.github.souqly.souqly.repository.OrderRepository;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	public Order createOrder(CreateOrderDTO dto) {
		Order order = new Order();

		order.setUserId(dto.getUserId());
		order.setUserEmail(dto.getUserEmail());
		order.setAddress(dto.getAddress());
		order.setStatus(dto.getStatus());
		order.setTotalAmount(dto.getTotalAmount());

		return orderRepository.createOrder(order);
	}

	public void updateOrderState(String orderId, OrderStatus orderStatus) {
		orderRepository.updateOrderState(orderId, orderStatus);
	}
	
	

}
