package com.github.souqly.souqly.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.github.souqly.souqly.model.Cart;
import com.github.souqly.souqly.model.CartItem;
import com.github.souqly.souqly.model.CartState;
import com.github.souqly.souqly.model.Category;
import com.github.souqly.souqly.model.Product;
import com.github.souqly.souqly.model.User;
import com.github.souqly.souqly.payload.response.CartItemResponse;
import com.github.souqly.souqly.payload.response.CartResponse;
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
		if (product == null)
			return null;
		ProductResponse productResponse = new ProductResponse();

		// Map category to category response
		CategoryResponse categoryResponse = mapCategoryToCategoryResponse(product.getProductCategory());
		productResponse.setProductCategory(categoryResponse);

		// Map user to user response
		UserResponse userResponse = mapUserToUserResponse(product.getProductSeller());
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

	public UserResponse mapUserToUserResponse(User user) {
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

	public CartItemResponse mapCartItem(CartItem cartItem, double currentPrice, double currentDiscount,
			double currentSpecialPrice, boolean availableForRequiredQuantity, boolean priceChangedSinceAdded) {
		CartItemResponse cartItemResponse = new CartItemResponse();
		ProductResponse productResponse = mapProductToProductResponse(cartItem.getProduct());

		cartItemResponse.setProductResponse(productResponse);
		
		cartItemResponse.setCartItemId(cartItem.getCartItemId());
		cartItemResponse.setCartId(cartItem.getCartId());
		cartItemResponse.setProductId(cartItem.getProductId());

		cartItemResponse.setPriceAtAddition(cartItem.getPriceAtAddition());
		cartItemResponse.setDiscountAtAddition(cartItem.getDiscountAtAddition());
		cartItemResponse.setSpecialPriceAtAddition(cartItem.getSpecialPriceAtAddition());

		cartItemResponse.setQuantity(cartItem.getQuantity());
		
		cartItemResponse.setCurrentPrice(currentPrice);
		cartItemResponse.setCurrentDiscount(currentDiscount);
		cartItemResponse.setCurrentSpecialPrice(currentSpecialPrice);


		cartItemResponse.setAvailableForRequiredQuantity(availableForRequiredQuantity);
		cartItemResponse.setPriceChangedSinceAdded(priceChangedSinceAdded);

		cartItemResponse.setCreatedAt(cartItem.getCreatedAt());
		cartItemResponse.setUpdatedAt(cartItem.getUpdatedAt());

		return cartItemResponse;
	}
	public CartResponse mapCartToResponse(Cart cart, List<CartItemResponse> items, long totalItems, double totalPriceAtAddition, double currentTotalPrice) {
	    CartResponse cartResponse = new CartResponse();
	    
	    cartResponse.setCartId(cart.getCartId());
	    cartResponse.setCustomerId(cart.getCustomerId());
	    cartResponse.setCartState(cart.getCartState());
	    cartResponse.setCreatedAt(cart.getCreatedAt());
	    cartResponse.setUpdatedAt(cart.getUpdatedAt());

	    cartResponse.setItems(items);
	    cartResponse.setTotalItems(totalItems);
	    cartResponse.setTotalPriceAtAddition(totalPriceAtAddition);
	    cartResponse.setCurrentTotalPrice(currentTotalPrice);

	    return cartResponse;
	}
}
