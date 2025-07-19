package com.github.souqly.souqly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.souqly.souqly.payload.request.AddCartProductRequest;
import com.github.souqly.souqly.payload.request.UpdateCartItemQuantityRequest;
import com.github.souqly.souqly.payload.response.ApiResponse;
import com.github.souqly.souqly.payload.response.CartItemResponse;
import com.github.souqly.souqly.payload.response.CartResponse;
import com.github.souqly.souqly.security.service.ActiveUser;
import com.github.souqly.souqly.service.CartService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

@Validated
@RestController
@RequestMapping("/api/cart")
public class CartController {

	@Autowired
	CartService cartService;

	@Autowired
	ActiveUser activeUser;

	@PostMapping("/add-product")
	public ResponseEntity<ApiResponse<CartItemResponse>> addCartProduct(
			@Valid @RequestBody AddCartProductRequest addCartProductRequest) {
		String userId = activeUser.getUserId();
		CartItemResponse data = cartService.addCartProduct(addCartProductRequest, userId);
		ApiResponse<CartItemResponse> apiResponse = new ApiResponse<>();
		apiResponse.setData(data);
		apiResponse.setSuccess(true);
		apiResponse.setMessage("Product add successfully");
		return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
	}

	@GetMapping("/view")
	public ResponseEntity<ApiResponse<CartResponse>> viewUserCart() {
		String userId = activeUser.getUserId();
		CartResponse data = cartService.viewUserCart(userId);
		ApiResponse<CartResponse> apiResponse = new ApiResponse<>();
		apiResponse.setData(data);
		apiResponse.setSuccess(true);
		apiResponse.setMessage("success");
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{cartItemId}")
	public ResponseEntity<ApiResponse<Void>> deleteCartItem(
			@PathVariable @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Invalid UUID format for cartItemId") String cartItemId) {
		String userId = activeUser.getUserId();
		cartService.removeCartItemFromCart(cartItemId, userId);

		ApiResponse<Void> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("item deleted from the cart successfully");

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/update/{cartItemId}")
	public ResponseEntity<ApiResponse<CartItemResponse>> updateCartItemQuantity(
			@PathVariable @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Invalid UUID format for cartItemId") String cartItemId,
			@Valid @RequestBody UpdateCartItemQuantityRequest cartItemQuantityRequest) {
		String userId = activeUser.getUserId();
		CartItemResponse data = cartService.updateCartItemInCart(cartItemQuantityRequest, cartItemId, userId);

		ApiResponse<CartItemResponse> apiResponse = new ApiResponse<>();
		apiResponse.setData(data);
		apiResponse.setSuccess(true);
		apiResponse.setMessage(
				data == null ? "the item deleted from your cart" : "item deleted from the cart successfully");

		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

}
