package com.github.souqly.souqly.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateCategoryRequest {
	
//	@Pattern(
//		    regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$",
//		    message = "Category ID must be a valid UUID"
//		)
//	private String categoryId;
	
    @NotBlank(message = "Category name must not be blank")
    @Size(min = 3, max = 50, message = "Category name must be between 3 and 50 characters")
    private String categoryName;

    @NotBlank(message = "Category details must not be blank")
    @Size(min = 5, max = 255, message = "Category details must be between 5 and 255 characters")
    private String categoryDetails;


	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryDetails() {
		return categoryDetails;
	}

	public void setCategoryDetails(String categoryDetails) {
		this.categoryDetails = categoryDetails;
	}
    
    
    
    
    

}
