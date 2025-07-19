package com.github.souqly.souqly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.souqly.souqly.payload.request.CreateOrderRequest;
import com.github.souqly.souqly.payload.response.ApiResponse;
import com.github.souqly.souqly.payload.response.CheckoutResponse;
import com.github.souqly.souqly.security.service.ActiveUser;
import com.github.souqly.souqly.service.CheckoutService;
import com.stripe.exception.StripeException;

import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

	@Autowired
	CheckoutService checkoutService;

	@Autowired
	ActiveUser activeUser;

	@PostMapping
	public ResponseEntity<ApiResponse<CheckoutResponse>> checkout(
			@Valid @RequestBody CreateOrderRequest createOrderRequest) {
		String userId = activeUser.getUserId();
		CheckoutResponse data;
		try {
			data = checkoutService.checkout(createOrderRequest, userId);
			ApiResponse<CheckoutResponse> apiResponse = new ApiResponse<CheckoutResponse>();
			apiResponse.setData(data);
			apiResponse.setSuccess(true);
			apiResponse.setMessage("checkout success");
			return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
		} catch (StripeException e) {
			ApiResponse<CheckoutResponse> apiResponse = new ApiResponse<CheckoutResponse>();
			apiResponse.setSuccess(false);
			apiResponse.setMessage("checkout failed");
			return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
