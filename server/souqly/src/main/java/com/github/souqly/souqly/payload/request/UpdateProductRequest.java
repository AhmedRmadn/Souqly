package com.github.souqly.souqly.payload.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateProductRequest {
	
	@NotBlank(message = "Product name is required")
	@Size(max = 100, message = "Product name must be at most 100 characters")
	private String productName;

	@Size(max = 255, message = "Image URL must be at most 255 characters")
	private String imageUrl;

	@NotBlank(message = "Product details are required")
	private String productDetails;

	@Min(value = 0, message = "Quantity must be zero or positive")
	private int quantity;

	@Min(value = 0, message = "Price must be non-negative")
	private double price;

	@Min(value = 0, message = "Discount must be at least 0%")
	@Max(value = 100, message = "Discount cannot exceed 100%")
	private double discount;

	@NotBlank(message = "Category ID is required")
	private String categoryId;

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

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	
	


}
