package com.github.souqly.souqly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.souqly.souqly.model.Category;
import com.github.souqly.souqly.payload.request.CreateCategoryRequest;
import com.github.souqly.souqly.payload.request.PageCategoryRequest;
import com.github.souqly.souqly.payload.request.UpdateCategoryRequest;
import com.github.souqly.souqly.payload.response.AllRecordsResponse;
import com.github.souqly.souqly.payload.response.CategoryResponse;
import com.github.souqly.souqly.payload.response.PagedResponse;
import com.github.souqly.souqly.repository.CategoryRepository;

import jakarta.validation.constraints.Pattern;

@Service
public class CategoryService {

	@Autowired
	CategoryRepository categoryRepository;

	public AllRecordsResponse<CategoryResponse> getAllGetegoeries() {
		List<Category> categories = categoryRepository.getAllGategories();
		List<CategoryResponse> responseList = categories.stream().map(this::mapCategoryToCategoryResponse).toList();
		AllRecordsResponse<CategoryResponse> data = new AllRecordsResponse<>();
		data.setContent(responseList);
		data.setTotalElements(responseList.size());

		return data;
	}

	private CategoryResponse mapCategoryToCategoryResponse(Category category) {
		CategoryResponse response = new CategoryResponse();
		response.setCategoryId(category.getCategoryId());
		response.setCategoryName(category.getCategoryName());
		response.setCategoryDetails(category.getCategoryDetails());
		response.setCreatedAt(category.getCreatedAt());
		response.setUpdatedAt(category.getUpdatedAt());
		return response;
	}

	public PagedResponse<CategoryResponse> getCategoryPage(PageCategoryRequest pageCategoryRequest) {
		int size = pageCategoryRequest.getPageSize();
		int page = pageCategoryRequest.getPageNumber();
		int offset = (Math.min(page, 1) - 1) * size;
		List<Category> categories = categoryRepository.findPageCategories(offset, size, pageCategoryRequest.getSortBy(),
				pageCategoryRequest.getSortOrder());
		long total = categoryRepository.countCategories();
		List<CategoryResponse> responseList = categories.stream().map(this::mapCategoryToCategoryResponse).toList();
		PagedResponse<CategoryResponse> pagedResponse = new PagedResponse<>(responseList, page, size, total);
		return pagedResponse;
	}

	public CategoryResponse createCategory(CreateCategoryRequest createCategoryRequest) {
		Category category = new Category();
		category.setCategoryName(createCategoryRequest.getCategoryName());
		category.setCategoryDetails(createCategoryRequest.getCategoryDetails());
		category = categoryRepository.save(category);
		return mapCategoryToCategoryResponse(category);
	}

	public CategoryResponse updateCategory(UpdateCategoryRequest updateCategoryRequest, String categoryId) {
		Category category = new Category();
		category.setCategoryName(updateCategoryRequest.getCategoryName());
		category.setCategoryDetails(updateCategoryRequest.getCategoryDetails());
		category.setCategoryId(categoryId);
		category = categoryRepository.update(category);
		category = categoryRepository.findById(category.getCategoryId());
		return mapCategoryToCategoryResponse(category);
	}

	public void deleteCategory(String categoryId) {
		categoryRepository.deleteCategory(categoryId);
	}

	public CategoryResponse getCategoryById(String categoryId) {
		Category category = categoryRepository.findById(categoryId);
		return mapCategoryToCategoryResponse(category);
	}

}
