package com.github.souqly.souqly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.github.souqly.souqly.payload.request.CreateAddressRequest;
import com.github.souqly.souqly.payload.request.UpdateAddressRequest;
import com.github.souqly.souqly.payload.response.AddressResponse;
import com.github.souqly.souqly.payload.response.AllRecordsResponse;
import com.github.souqly.souqly.payload.response.ApiResponse;
import com.github.souqly.souqly.security.service.ActiveUser;
import com.github.souqly.souqly.service.AddressService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

@RestController
@Validated
@RequestMapping("/api/address")
public class AddressContoller {

	@Autowired
	private ActiveUser activeUser;

	@Autowired
	private AddressService addressService;

	// Create address
	@PostMapping("/add")
	public ResponseEntity<ApiResponse<AddressResponse>> createAddress(@Valid @RequestBody CreateAddressRequest request) {
		String userId = activeUser.getUserId();
		AddressResponse data = addressService.addUserAddress(request, userId);
		ApiResponse<AddressResponse> apiResponse = new ApiResponse<>();
		apiResponse.setData(data);
		apiResponse.setSuccess(true);
		apiResponse.setMessage("Address added successfully");
		return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
	}

	// Get specific address by ID
	@GetMapping("/get/{addressId}")
	public ResponseEntity<ApiResponse<AddressResponse>> getAddressById(@PathVariable String addressId) {
		String userId = activeUser.getUserId();
		AddressResponse data = addressService.findAddressById(addressId, userId);
		ApiResponse<AddressResponse> apiResponse = new ApiResponse<>();
		apiResponse.setData(data);
		apiResponse.setSuccess(true);
		apiResponse.setMessage("Address retrieved successfully");
		return ResponseEntity.ok(apiResponse);
	}

	// Delete specific address
	@DeleteMapping("/delete/{addressId}")
	public ResponseEntity<ApiResponse<Void>> deleteAddress(@PathVariable String addressId) {
		String userId = activeUser.getUserId();
		addressService.deleteAddress(addressId, userId);
		ApiResponse<Void> apiResponse = new ApiResponse<>();
		apiResponse.setSuccess(true);
		apiResponse.setMessage("Address deleted successfully");
		return ResponseEntity.ok(apiResponse);
	}

	// Update address
	@PutMapping("/update/{addressId}")
	public ResponseEntity<ApiResponse<AddressResponse>> updateAddress(
			@PathVariable @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Invalid UUID format for addressId") String addressId,
			@Valid @RequestBody UpdateAddressRequest request) {
		String userId = activeUser.getUserId();
		AddressResponse data = addressService.updateUserAddress(request, addressId, userId);
		ApiResponse<AddressResponse> apiResponse = new ApiResponse<>();
		apiResponse.setData(data);
		apiResponse.setSuccess(true);
		apiResponse.setMessage("Address updated successfully");
		return ResponseEntity.ok(apiResponse);
	}

	// Get all addresses for the logged-in user
	@GetMapping("/get-all")
	public ResponseEntity<ApiResponse<AllRecordsResponse<AddressResponse>>> getUserAddresses() {
		String userId = activeUser.getUserId();
		AllRecordsResponse<AddressResponse> data = addressService.getUserAddresses(userId);
		ApiResponse<AllRecordsResponse<AddressResponse>> apiResponse = new ApiResponse<>();
		apiResponse.setData(data);
		apiResponse.setSuccess(true);
		apiResponse.setMessage("All addresses retrieved successfully");
		return ResponseEntity.ok(apiResponse);
	}
}
