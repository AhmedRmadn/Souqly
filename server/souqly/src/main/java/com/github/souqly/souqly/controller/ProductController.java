package com.github.souqly.souqly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.souqly.souqly.payload.request.PageProductRequest;
import com.github.souqly.souqly.payload.request.UpdateCategoryRequest;
import com.github.souqly.souqly.payload.request.UpdateProductRequest;
import com.github.souqly.souqly.payload.request.CreateProductRequest;
import com.github.souqly.souqly.payload.response.AllRecordsResponse;
import com.github.souqly.souqly.payload.response.ApiResponse;
import com.github.souqly.souqly.payload.response.CategoryResponse;
import com.github.souqly.souqly.payload.response.PagedResponse;
import com.github.souqly.souqly.payload.response.ProductResponse;
import com.github.souqly.souqly.security.service.ActiveUser;
import com.github.souqly.souqly.security.service.UserDetailsImpl;
import com.github.souqly.souqly.service.ProductService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

@Validated
@RestController
@RequestMapping("/api/product")
public class ProductController {

	@Autowired
	ProductService prodcutService;

	@Autowired
	ActiveUser activeUser;

	@GetMapping("/get-all")
	public ResponseEntity<ApiResponse<AllRecordsResponse<ProductResponse>>> getAllProducts() {
		AllRecordsResponse<ProductResponse> data = prodcutService.getAllProducts();
		ApiResponse<AllRecordsResponse<ProductResponse>> apiResponse = new ApiResponse<>();
		apiResponse.setData(data);
		apiResponse.setSuccess(true);
		apiResponse.setMessage("Products fetched successfully");
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@GetMapping("/get-page")
	public ResponseEntity<ApiResponse<PagedResponse<ProductResponse>>> getProductsPage(
			@Valid PageProductRequest pageProductRequest) {
		PagedResponse<ProductResponse> data = prodcutService.getProductsPage(pageProductRequest);
		ApiResponse<PagedResponse<ProductResponse>> apiResponse = new ApiResponse<>();
		apiResponse.setData(data);
		apiResponse.setSuccess(true);
		apiResponse.setMessage("Products fetched successfully");
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@GetMapping("/get-category/{categoryId}")
	public ResponseEntity<ApiResponse<AllRecordsResponse<ProductResponse>>> getCategoryProducts(
			@PathVariable @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Invalid UUID format for categoryId") String categoryId) {
		AllRecordsResponse<ProductResponse> data = prodcutService.getCategoryProducts(categoryId);
		ApiResponse<AllRecordsResponse<ProductResponse>> apiResponse = new ApiResponse<>();
		apiResponse.setData(data);
		apiResponse.setSuccess(true);
		apiResponse.setMessage("Products fetched successfully");
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@GetMapping("/{productId}")
	public ResponseEntity<ApiResponse<ProductResponse>> getProductById(
			@PathVariable @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Invalid UUID format for categoryId") String productId) {
		ProductResponse data = prodcutService.getProductById(productId);
		ApiResponse<ProductResponse> apiResponse = new ApiResponse<>();
		apiResponse.setData(data);
		apiResponse.setSuccess(true);
		apiResponse.setMessage("Product fetched successfully");
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@PostMapping("/add")
	public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
			@Valid @RequestBody CreateProductRequest createProductRequest) {
		String userId = activeUser.getUserId();
		ProductResponse data = prodcutService.createProduct(createProductRequest, userId);
		ApiResponse<ProductResponse> apiResponse = new ApiResponse<>();
		apiResponse.setData(data);
		apiResponse.setSuccess(true);
		apiResponse.setMessage("Product created successfully");
		return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
	}

	@PutMapping("/update/{productId}")
	public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
			@Valid @RequestBody UpdateProductRequest updateProductRequest,
			@PathVariable @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Invalid UUID format for productId") String productId) {
		String activeUserId = activeUser.getUserId();
		ProductResponse data = prodcutService.updateProduct(updateProductRequest, productId, activeUserId);
		ApiResponse<ProductResponse> apiResponse = new ApiResponse<>();
		apiResponse.setData(data);
		apiResponse.setSuccess(true);
		apiResponse.setMessage("Product created successfully");
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{prodcutId}")
	public ResponseEntity<ApiResponse<Void>> deleteCategory(
			@PathVariable @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Invalid UUID format for prodcutId") String prodcutId) {
		String activeUserId = activeUser.getUserId();
		prodcutService.deleteProduct(prodcutId, activeUserId);
		ApiResponse<Void> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Category deleted successfully");

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
