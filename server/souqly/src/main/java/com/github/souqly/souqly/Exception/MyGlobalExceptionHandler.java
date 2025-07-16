package com.github.souqly.souqly.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.github.souqly.souqly.payload.response.ApiResponse;

import jakarta.validation.ConstraintViolationException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class MyGlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		Map<String, String> response = new HashMap<>();
		e.getBindingResult().getAllErrors().forEach(err -> {
			String fieldName = ((FieldError) err).getField();
			String message = err.getDefaultMessage();
			response.put(fieldName, message);
		});
		return new ResponseEntity<Map<String, String>>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(ConstraintViolationException e) {
		ApiResponse<Void> response = new ApiResponse<Void>();
		response.setSuccess(false);
		response.setMessage(e.getMessage());
		return new ResponseEntity<ApiResponse<Void>>(response, HttpStatus.BAD_REQUEST);
	}


	@ExceptionHandler(UnAuthorizedException.class)
	public ResponseEntity<ApiResponse<Void>> AnAuthException(UnAuthorizedException e) {
		ApiResponse<Void> response = new ApiResponse<Void>();
		response.setSuccess(false);
		response.setMessage(e.getMessage());
		return new ResponseEntity<ApiResponse<Void>>(response, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse<Void>> myResourceNotFoundException(ResourceNotFoundException e) {
		ApiResponse<Void> response = new ApiResponse<>();
		response.setMessage(e.getMessage());
		response.setSuccess(false);
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<ApiResponse<Void>> handleDatabaseException(DatabaseException e) {
		ApiResponse<Void> response = new ApiResponse<>();
		response.setMessage(e.getMessage());
		response.setSuccess(false);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ApiResponse<Void>> handleApiException(ApiException e) {
		ApiResponse<Void> response = new ApiResponse<>();
		response.setMessage(e.getMessage());
		response.setSuccess(false);
		return new ResponseEntity<>(response, e.getHttpStatus());
	}

//	@ExceptionHandler(ResourceNotFoundException.class)
//	public ResponseEntity<APIResponse> myResourceNotFoundException(ResourceNotFoundException e) {
//		String message = e.getMessage();
//		APIResponse apiResponse = new APIResponse();
//		apiResponse.setMessage(message);
//		apiResponse.setStatus(false);
//		return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
//	}
//
//	@ExceptionHandler(APIException.class)
//	public ResponseEntity<APIResponse> myAPIException(APIException e) {
//		String message = e.getMessage();
//		APIResponse apiResponse = new APIResponse();
//		apiResponse.setMessage(message);
//		apiResponse.setStatus(false);
//		return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
//	}
}
