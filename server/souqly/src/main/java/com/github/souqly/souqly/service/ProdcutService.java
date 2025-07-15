package com.github.souqly.souqly.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.souqly.souqly.Exception.ResourceNotFoundException;
import com.github.souqly.souqly.Exception.UnAuthorizedException;
import com.github.souqly.souqly.model.Product;
import com.github.souqly.souqly.payload.request.CreateProductRequest;
import com.github.souqly.souqly.payload.request.PageProductRequest;
import com.github.souqly.souqly.payload.request.UpdateProductRequest;
import com.github.souqly.souqly.payload.response.AllRecordsResponse;
import com.github.souqly.souqly.payload.response.CategoryResponse;
import com.github.souqly.souqly.payload.response.PagedResponse;
import com.github.souqly.souqly.payload.response.ProductResponse;
import com.github.souqly.souqly.repository.ProductRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

@Service
public class ProdcutService {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	Mapper mapper;

	public AllRecordsResponse<ProductResponse> getAllProducts() {
		List<Product> products = productRepository.getAll();
		List<ProductResponse> responseList = products.stream().map(mapper::mapProductToProductResponse).toList();
		AllRecordsResponse<ProductResponse> data = new AllRecordsResponse<>();
		data.setContent(responseList);
		data.setTotalElements(responseList.size());
		return data;
	}

	public AllRecordsResponse<ProductResponse> getCategoryProducts(String categoryId) {
		List<Product> products = productRepository.getProductsPerCategory(categoryId);
		List<ProductResponse> responseList = products.stream().map(mapper::mapProductToProductResponse).toList();
		AllRecordsResponse<ProductResponse> data = new AllRecordsResponse<>();
		data.setContent(responseList);
		data.setTotalElements(responseList.size());
		return data;
	}

	public PagedResponse<ProductResponse> getProductsPage(PageProductRequest pageProductRequest) {
		int size = pageProductRequest.getPageSize();
		int page = pageProductRequest.getPageNumber();
		int offset = (Math.min(page, 1) - 1) * size;
		List<Product> products = productRepository.getPageProducts(offset, size, pageProductRequest.getSortBy(),
				pageProductRequest.getSortOrder());
		long total = productRepository.countProducts();
		List<ProductResponse> responseList = products.stream().map(mapper::mapProductToProductResponse).toList();
		PagedResponse<ProductResponse> pagedResponse = new PagedResponse<>(responseList, page, size, total);
		return pagedResponse;
	}

	public ProductResponse createProduct(CreateProductRequest createProductRequest, String userId) {
		Product product = new Product();

		product.setProductName(createProductRequest.getProductName());
		product.setImageUrl(createProductRequest.getImageUrl());
		product.setProductDetails(createProductRequest.getProductDetails());
		product.setQuantity(createProductRequest.getQuantity());
		product.setPrice(createProductRequest.getPrice());
		product.setDiscount(createProductRequest.getDiscount());
		product.setCategoryId(createProductRequest.getCategoryId());
		product.setSellerId(userId);

		// Compute special price
		double specialPrice = product.getPrice() - (product.getPrice() * product.getDiscount() / 100.0);
		product.setSpecialPrice(specialPrice);

		// Save to DB
		product = productRepository.save(product); // Assuming you have a save() method

		// Map to response (if you have a mapper method)
		return mapper.mapProductToProductResponse(product);
	}

	public void deleteProduct(String productId, String activeUserId) {
		String productSeller = productRepository.findProductSeller(productId);
		if (productSeller == null) {
			throw new ResourceNotFoundException("Product with id " + productId + " not found.");
		}
		if (!productSeller.equals(activeUserId)) {
			throw new UnAuthorizedException("UNAUTHORIZED to access this product");
		}
		productRepository.deleteProduct(productId);

	}

	public ProductResponse updateProduct(UpdateProductRequest updateProductRequest, String productId,
			String activeUserId) {
		String productSeller = productRepository.findProductSeller(productId);
		if (productSeller == null) {
			throw new ResourceNotFoundException("Product with id " + productId + " not found.");
		}
		if (!productSeller.equals(activeUserId)) {
			throw new UnAuthorizedException("UNAUTHORIZED to access this product");
		}
		Product product = new Product();
		product.setProductId(productId);
		product.setProductName(updateProductRequest.getProductName());
		product.setImageUrl(updateProductRequest.getImageUrl());
		product.setProductDetails(updateProductRequest.getProductDetails());
		product.setQuantity(updateProductRequest.getQuantity());
		product.setPrice(updateProductRequest.getPrice());
		product.setDiscount(updateProductRequest.getDiscount());
		product.setCategoryId(updateProductRequest.getCategoryId());

		// Compute special price
		double specialPrice = product.getPrice() - (product.getPrice() * product.getDiscount() / 100.0);
		product.setSpecialPrice(specialPrice);

		product = productRepository.updateProduct(product);
		product = productRepository.findProductById(productId);

		return mapper.mapProductToProductResponse(product);
	}

	public ProductResponse getProductById(String productId) {
		Product product = productRepository.findProductCategoryUserById(productId);
		return mapper.mapProductToProductResponse(product);
	}

}
