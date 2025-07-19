package com.github.souqly.souqly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.github.souqly.souqly.payload.response.ApiResponse;
import com.github.souqly.souqly.payload.response.ProductImageResponse;
import com.github.souqly.souqly.security.service.ActiveUser;
import com.github.souqly.souqly.service.ProductImageService;

@RestController
@RequestMapping("/api/product")
public class ProductImageController {

	@Autowired
	ActiveUser activeUser;

	@Autowired
	ProductImageService productImageService;

	@PostMapping("/{productId}/image")
	public ResponseEntity<ApiResponse<ProductImageResponse>> uploadProductImage(@PathVariable String productId,
			@RequestParam("file") MultipartFile file) {
		String userId = activeUser.getUserId();
		ProductImageResponse data = productImageService.uploadImageForProduct(file, productId, activeUser.getUserId());
		ApiResponse<ProductImageResponse> apiResponse = new ApiResponse<ProductImageResponse>();
		apiResponse.setData(data);
		apiResponse.setSuccess(true);
		apiResponse.setMessage("image uploaded successfully");
		return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
	}
}
