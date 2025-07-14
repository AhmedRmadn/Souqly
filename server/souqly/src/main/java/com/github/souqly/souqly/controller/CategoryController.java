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

import com.github.souqly.souqly.payload.request.CreateCategoryRequest;
import com.github.souqly.souqly.payload.request.PageCategoryRequest;
import com.github.souqly.souqly.payload.request.UpdateCategoryRequest;
import com.github.souqly.souqly.payload.response.AllRecordsResponse;
import com.github.souqly.souqly.payload.response.ApiResponse;
import com.github.souqly.souqly.payload.response.CategoryResponse;
import com.github.souqly.souqly.payload.response.PagedResponse;
import com.github.souqly.souqly.service.CategoryService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

@Validated
@RestController
@RequestMapping("/api/category")
public class CategoryController {

	@Autowired
	CategoryService categoryService;

	@GetMapping("/{categoryId}")
	public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(
			@PathVariable @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Invalid UUID format for categoryId") String categoryId) {

		CategoryResponse data = categoryService.getCategoryById(categoryId);

		ApiResponse<CategoryResponse> apiResponse = new ApiResponse<>();
		apiResponse.setSuccess(true);
		apiResponse.setData(data);
		apiResponse.setMessage("Category fetched successfully");

		return ResponseEntity.ok(apiResponse);
	}

	@GetMapping("/get-all")
	public ResponseEntity<ApiResponse<AllRecordsResponse<CategoryResponse>>> getAllCategories() {
		AllRecordsResponse<CategoryResponse> data = categoryService.getAllGetegoeries();
		ApiResponse<AllRecordsResponse<CategoryResponse>> apiResponse = new ApiResponse<>();
		apiResponse.setData(data);
		apiResponse.setSuccess(true);
		apiResponse.setMessage("Categories fetched successfully");
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@GetMapping("/get-page")
	public ResponseEntity<ApiResponse<PagedResponse<CategoryResponse>>> getCategoryPage(
			@Valid PageCategoryRequest pageCategoryRequest) {
		PagedResponse<CategoryResponse> data = categoryService.getCategoryPage(pageCategoryRequest);
		ApiResponse<PagedResponse<CategoryResponse>> apiResponse = new ApiResponse<>();
		apiResponse.setData(data);
		apiResponse.setSuccess(true);
		apiResponse.setMessage("Categories fetched successfully");
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@PostMapping("/create")
	public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
			@Valid @RequestBody CreateCategoryRequest createCategoryRequest) {
		CategoryResponse data = categoryService.createCategory(createCategoryRequest);
		ApiResponse<CategoryResponse> apiResponse = new ApiResponse<>();
		apiResponse.setData(data);
		apiResponse.setSuccess(true);
		apiResponse.setMessage("Categories created successfully");
		return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
	}

	@PutMapping("/update/{categoryId}")
	public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
			@Valid @RequestBody UpdateCategoryRequest updateCategoryRequest,
			@PathVariable @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Invalid UUID format for categoryId") String categoryId) {
		CategoryResponse data = categoryService.updateCategory(updateCategoryRequest, categoryId);
		ApiResponse<CategoryResponse> apiResponse = new ApiResponse<>();
		apiResponse.setData(data);
		apiResponse.setSuccess(true);
		apiResponse.setMessage("Categories updated successfully");
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{categoryId}")
	public ResponseEntity<ApiResponse<Void>> deleteCategory(
			@PathVariable @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Invalid UUID format for categoryId") String categoryId) {

		categoryService.deleteCategory(categoryId);

		ApiResponse<Void> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Category deleted successfully");

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
