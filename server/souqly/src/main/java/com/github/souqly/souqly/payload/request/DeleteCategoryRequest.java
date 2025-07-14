package com.github.souqly.souqly.payload.request;

import jakarta.validation.constraints.Pattern;

public class DeleteCategoryRequest {
	@Pattern(
		    regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$",
		    message = "Category ID must be a valid UUID"
		)
	private String categoryId;

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	
	
	

}
