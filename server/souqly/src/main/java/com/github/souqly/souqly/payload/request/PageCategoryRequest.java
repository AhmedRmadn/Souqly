package com.github.souqly.souqly.payload.request;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public class PageCategoryRequest {

	@Min(value = 1, message = "Page number must be at least 1")
	private Integer pageNumber = 1;

	@Min(value = 1, message = "Page size must be at least 1")
	@Max(value = 100, message = "Page size must not exceed 100")
	private Integer pageSize = 50;

	@Pattern(regexp = "categoryName|createdAt", message = "SortBy must be a valid field")
	private String sortBy = "createdAt";

	@Pattern(regexp = "asc|desc", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Sort order must be 'asc' or 'desc'")
	private String sortOrder = "asc";

	private String mapSortingField(String field) {
		if (field == null)
			return "created_at";
		switch (field.toLowerCase()) {
		case "categoryname": {
			return "category_name";
		}
		default:
			return "created_at";
		}
	}

	// Getters and setters
	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getSortBy() {
		return mapSortingField(sortBy);
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
}
