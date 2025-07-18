package com.github.souqly.souqly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.souqly.souqly.model.OrderItem;
import com.github.souqly.souqly.payload.request.CreateOrderItemDTO;
import com.github.souqly.souqly.repository.OrderItemRepository;

@Service
public class OrderItemService {

	@Autowired
	private OrderItemRepository orderItemRepository;

	public OrderItem createOrderItem(CreateOrderItemDTO dto) {
		OrderItem orderItem = new OrderItem();

		orderItem.setOrderId(dto.getOrderId());
		orderItem.setProductId(dto.getProductId());
		orderItem.setProductName(dto.getProductName());
		orderItem.setProductImageUrl(dto.getProductImageUrl());
		orderItem.setProductDetails(dto.getProductDetails());
		orderItem.setQuantity(dto.getQuantity());
		orderItem.setPrice(dto.getPrice());
		orderItem.setDiscount(dto.getDiscount());
		orderItem.setSpecialPrice(dto.getSpecialPrice());
		orderItem.setSellerId(dto.getSellerId());
		orderItem.setSellerEmail(dto.getSellerEmail());

		return orderItemRepository.createOrderItem(orderItem);
	}
}
