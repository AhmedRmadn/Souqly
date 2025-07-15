package com.github.souqly.souqly.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.souqly.souqly.Exception.DatabaseException;
import com.github.souqly.souqly.Exception.DeleteDatabaseException;
import com.github.souqly.souqly.Exception.FetchDatabaseException;
import com.github.souqly.souqly.Exception.InsertDatabaseException;
import com.github.souqly.souqly.Exception.UpdateDatabaseException;
import com.github.souqly.souqly.model.Category;
import com.github.souqly.souqly.model.Product;
import com.github.souqly.souqly.repository.rowmapper.CategoryRowMapper;
import com.github.souqly.souqly.repository.rowmapper.ProductCategoryUserRowMapper;
import com.github.souqly.souqly.repository.rowmapper.ProductRowMapper;

@Repository
public class ProductRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	private String generateUUIDForProduct() {
		return UUID.randomUUID().toString();
	}

	public List<Product> getAll() {
		String query = """
				SELECT * FROM products AS p
				JOIN categories AS c ON
				p.category_id = c.category_id
				JOIN users AS u ON
				p.seller_id = u.user_id
				ORDER BY p.created_at DESC
				""";
		List<Product> products = jdbcTemplate.query(query, new ProductCategoryUserRowMapper());
		return products;
	}

	public List<Product> getProductsPerCategory(String categoryId) {
		String query = """
				SELECT * FROM products AS p
				JOIN categories AS c ON
				p.category_id = c.category_id
				JOIN users AS u ON
				p.seller_id = u.user_id
				WHERE c.category_id = ?
				ORDER BY p.created_at DESC
				""";
		return jdbcTemplate.query(query, new ProductCategoryUserRowMapper(), categoryId);
	}

	public List<Product> getPageProducts(int offset, int limit, String sortBy, String sortOrder) {
		String query = String.format("""
				SELECT * FROM products AS p
				JOIN categories AS c ON
				p.category_id = c.category_id
				JOIN users AS u ON
				p.seller_id = u.user_id
				ORDER BY p.%s %s
				LIMIT ? OFFSET ?
				""", sortBy, sortOrder);
		return jdbcTemplate.query(query, new ProductCategoryUserRowMapper(), limit, offset);
	}

	public long countProducts() {
		String query = """
				SELECT COUNT(*) FROM products
				""";
		long count = jdbcTemplate.queryForObject(query, Long.class);
		return count;
	}

	public Product save(Product product) {
		product.setProductId(generateUUIDForProduct());
		product.setCreatedAt(LocalDateTime.now());
		product.setUpdatedAt(LocalDateTime.now());

		String query = """
					INSERT INTO products (
						product_id, product_name, product_details,
						quantity, price, discount, special_price,
						category_id, seller_id, created_at, updated_at
					) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
				""";

		int row = jdbcTemplate.update(query, product.getProductId(), product.getProductName(),
				product.getProductDetails(), product.getQuantity(), product.getPrice(), product.getDiscount(),
				product.getSpecialPrice(), product.getCategoryId(), product.getSellerId(), product.getCreatedAt(),
				product.getUpdatedAt());
		if (row != 1)
			throw new InsertDatabaseException("failed to inseret into table products");
		return product;
	}

	public String findProductSeller(String productId) {
		String query = "SELECT seller_id from products where product_id = ?";
		try {
			String sellerId = jdbcTemplate.queryForObject(query, String.class, productId);
			return sellerId;
		} catch (DataAccessException e) {
			throw new FetchDatabaseException("Failed to fetch seller_id for product_id " + productId);
		}
	}

	public boolean deleteProduct(String productId) {
		String query = """
				    DELETE FROM products
				    WHERE product_id = ?
				""";
		int rows = jdbcTemplate.update(query, productId);

		if (rows == 1) {
			return true;
		} else if (rows == 0) {
			throw new DeleteDatabaseException("Product not found for ID: " + productId);
		} else {
			throw new DeleteDatabaseException("Unexpected row count (" + rows + ") when deleting Product " + productId);
		}

	}

	public Product updateProduct(Product product) {
		String query = """
				UPDATE products
				SET product_name = ?,
				    product_details = ?,
				    quantity = ?,
				    price = ?,
				    discount = ?,
				    special_price = ?,
				    category_id = ?,
				    updated_at = ?
				WHERE product_id = ?
				""";

		product.setUpdatedAt(LocalDateTime.now());

		int rows = jdbcTemplate.update(query, product.getProductName(),
				product.getProductDetails(), product.getQuantity(), product.getPrice(), product.getDiscount(),
				product.getSpecialPrice(), product.getCategoryId(), product.getUpdatedAt(), product.getProductId());

		if (rows == 1) {
			return product;
		} else {
			throw new UpdateDatabaseException("failed to update Product for productId " + product.getProductId());
		}
	}

	public Product findProductById(String productId) {
		String query = """
					SELECT * FROM products AS p
					WHERE product_id = ?
				""";
		try {
			Product product = jdbcTemplate.queryForObject(query, new ProductRowMapper(), productId);
			return product;
		} catch (EmptyResultDataAccessException ex) {
			throw new FetchDatabaseException("Database error while fetching products with ID: " + productId);
		} catch (DataAccessException ex) {
			throw new FetchDatabaseException("Database error while fetching products with ID: " + productId);
		}
	}

	public Product findProductCategoryUserById(String productId) {
		String query = """
				SELECT * FROM products AS p
				JOIN categories AS c ON
				p.category_id = c.category_id
				JOIN users AS u ON
				p.seller_id = u.user_id
				WHERE p.product_id = ?
				""";
		try {
			Product product = jdbcTemplate.queryForObject(query, new ProductCategoryUserRowMapper(), productId);
			return product;
		} catch (EmptyResultDataAccessException ex) {
			throw new FetchDatabaseException("Database error while fetching products with ID: " + productId);
		} catch (DataAccessException ex) {
			throw new FetchDatabaseException("Database error while fetching products with ID: " + productId);
		}
	}

}
