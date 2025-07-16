package com.github.souqly.souqly.payload.response;

import java.time.LocalDateTime;
import java.util.List;

import com.github.souqly.souqly.model.CartState;

public class CartResponse {
	
	private List<CartItemResponse> items;
	private long totalItems;
	private String cartId;
	private String customerId;
	private CartState cartState;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private double totalPriceAtAddition;
	private double currentTotalPrice;
	public List<CartItemResponse> getItems() {
		return items;
	}
	public void setItems(List<CartItemResponse> items) {
		this.items = items;
	}
	public long getTotalItems() {
		return totalItems;
	}
	public void setTotalItems(long totalItems) {
		this.totalItems = totalItems;
	}
	public String getCartId() {
		return cartId;
	}
	public void setCartId(String cartId) {
		this.cartId = cartId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public CartState getCartState() {
		return cartState;
	}
	public void setCartState(CartState cartState) {
		this.cartState = cartState;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	public double getTotalPriceAtAddition() {
		return totalPriceAtAddition;
	}
	public void setTotalPriceAtAddition(double totalPriceAtAddition) {
		this.totalPriceAtAddition = totalPriceAtAddition;
	}
	public double getCurrentTotalPrice() {
		return currentTotalPrice;
	}
	public void setCurrentTotalPrice(double currentTotalPrice) {
		this.currentTotalPrice = currentTotalPrice;
	}
	
	
	

	
	

}
