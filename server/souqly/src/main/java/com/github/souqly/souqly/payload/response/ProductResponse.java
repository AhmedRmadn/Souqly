package com.github.souqly.souqly.payload.response;

import java.time.LocalDateTime;


public class ProductResponse {

	private String productId;
	private String productName;
	private String imageUrl;
	private String productDetails;
	private int quantity;
	private double price;
	private double discount;
	private double specialPrice;
	private String categoryId; // still needed for DB mapping
	private CategoryResponse productCategory; // full category object
	private String sellerId;
	private UserResponse productSeller;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(String productDetails) {
		this.productDetails = productDetails;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getSpecialPrice() {
		return specialPrice;
	}

	public void setSpecialPrice(double specialPrice) {
		this.specialPrice = specialPrice;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public CategoryResponse getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(CategoryResponse productCategory) {
		this.productCategory = productCategory;
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

	public UserResponse getProductSeller() {
		return productSeller;
	}

	public void setProductSeller(UserResponse productSeller) {
		this.productSeller = productSeller;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	
	
	
	

}
