package com.github.souqly.souqly.model;

import java.time.LocalDateTime;

public class CartItem {

	private String cartItemId;
	private String cartId;
	private String productId;

	private double priceAtAddition;
	private double discountAtAddition;
	private double specialPriceAtAddition;
	private int quantity;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	private Product product;

	// Constructors
	public CartItem() {
	}

	// Getters and Setters

	public String getCartItemId() {
		return cartItemId;
	}

	public void setCartItemId(String cartItemId) {
		this.cartItemId = cartItemId;
	}

	public String getCartId() {
		return cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public double getPriceAtAddition() {
		return priceAtAddition;
	}

	public void setPriceAtAddition(double priceAtAddition) {
		this.priceAtAddition = priceAtAddition;
	}

	public double getDiscountAtAddition() {
		return discountAtAddition;
	}

	public void setDiscountAtAddition(double discountAtAddition) {
		this.discountAtAddition = discountAtAddition;
	}

	public double getSpecialPriceAtAddition() {
		return specialPriceAtAddition;
	}

	public void setSpecialPriceAtAddition(double specialPriceAtAddition) {
		this.specialPriceAtAddition = specialPriceAtAddition;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	
}
