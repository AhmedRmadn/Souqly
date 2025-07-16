package com.github.souqly.souqly.model;

import java.time.LocalDateTime;

public class Cart {
	private String cartId;
	private String customerId;
	private CartState cartState;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
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
	
	

	

}
