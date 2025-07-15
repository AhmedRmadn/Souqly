package com.github.souqly.souqly.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.github.souqly.souqly.model.Category;
import com.github.souqly.souqly.model.Product;
import com.github.souqly.souqly.model.User;
import com.github.souqly.souqly.payload.response.CategoryResponse;
import com.github.souqly.souqly.payload.response.ProductResponse;
import com.github.souqly.souqly.payload.response.UserResponse;

@Component
public class Mapper {

	public CategoryResponse mapCategoryToCategoryResponse(Category category) {
		if (category == null)
			return null;
		CategoryResponse response = new CategoryResponse();
		response.setCategoryId(category.getCategoryId());
		response.setCategoryName(category.getCategoryName());
		response.setCategoryDetails(category.getCategoryDetails());
		response.setCreatedAt(category.getCreatedAt());
		response.setUpdatedAt(category.getUpdatedAt());
		return response;
	}

	public ProductResponse mapProductToProductResponse(Product product) {
		ProductResponse productResponse = new ProductResponse();

		// Map category to category response
		CategoryResponse categoryResponse = mapCategoryToCategoryResponse(product.getProductCategory());
		productResponse.setProductCategory(categoryResponse);

		// Map user to user response
		UserResponse userResponse = mapProductToProductResponse(product.getProductSeller());
		productResponse.setProductSeller(userResponse);

		// Map product fields
		productResponse.setProductId(product.getProductId());
		productResponse.setProductName(product.getProductName());
		productResponse.setImageUrl(product.getImageUrl());
		productResponse.setProductDetails(product.getProductDetails());
		productResponse.setQuantity(product.getQuantity());
		productResponse.setPrice(product.getPrice());
		productResponse.setDiscount(product.getDiscount());
		productResponse.setSpecialPrice(product.getSpecialPrice());
		productResponse.setCreatedAt(product.getCreatedAt());
		productResponse.setUpdatedAt(product.getUpdatedAt());
		productResponse.setCategoryId(product.getCategoryId());
		productResponse.setSellerId(product.getSellerId());

		return productResponse;
	}

	public UserResponse mapProductToProductResponse(User user) {
		if (user == null)
			return null;
		UserResponse userResponse = new UserResponse();
		userResponse.setUserId(user.getUserId());
		userResponse.setUserName(user.getUserName());
		userResponse.setFirstName(user.getFirstName());
		userResponse.setLastName(user.getLastName());
		userResponse.setEmail(user.getEmail());
		userResponse.setCreatedAt(user.getCreatedAt());
		userResponse.setUpdatedAt(user.getUpdatedAt());
		return userResponse;
	}

}
