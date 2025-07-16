package com.github.souqly.souqly.payload.response;

import java.time.LocalDateTime;

public class CartItemResponse {

	private String cartItemId;
	private String cartId;
	private String productId;

	private double priceAtAddition;
	private double discountAtAddition;
	private double specialPriceAtAddition;
	private int quantity;

	private double currentPrice;
	private double currentDiscount;
	private double currentSpecialPrice;
	private boolean availableForRequiredQuantity;
	private boolean priceChangedSinceAdded;
	private boolean productDeleted;
	
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	private ProductResponse productResponse;

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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}

	public double getCurrentDiscount() {
		return currentDiscount;
	}

	public void setCurrentDiscount(double currentDiscount) {
		this.currentDiscount = currentDiscount;
	}

	public boolean isAvailableForRequiredQuantity() {
		return availableForRequiredQuantity;
	}

	public void setAvailableForRequiredQuantity(boolean availableForRequiredQuantity) {
		this.availableForRequiredQuantity = availableForRequiredQuantity;
	}
	
	public boolean isPriceChangedSinceAdded() {
		return priceChangedSinceAdded;
	}

	public void setPriceChangedSinceAdded(boolean priceChangedSinceAdded) {
		this.priceChangedSinceAdded = priceChangedSinceAdded;
	}

	public boolean isProductDeleted() {
		return productDeleted;
	}

	public void setProductDeleted(boolean productDeleted) {
		this.productDeleted = productDeleted;
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

	public ProductResponse getProductResponse() {
		return productResponse;
	}

	public void setProductResponse(ProductResponse productResponse) {
		this.productResponse = productResponse;
	}

	public double getSpecialPriceAtAddition() {
		return specialPriceAtAddition;
	}

	public void setSpecialPriceAtAddition(double specialPriceAtAddition) {
		this.specialPriceAtAddition = specialPriceAtAddition;
	}

	public double getCurrentSpecialPrice() {
		return currentSpecialPrice;
	}

	public void setCurrentSpecialPrice(double currentSpecialPrice) {
		this.currentSpecialPrice = currentSpecialPrice;
	}
	
	

	
	

}
