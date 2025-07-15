package com.github.souqly.souqly.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.souqly.souqly.Exception.DeleteDatabaseException;
import com.github.souqly.souqly.Exception.FetchDatabaseException;
import com.github.souqly.souqly.Exception.InsertDatabaseException;
import com.github.souqly.souqly.Exception.UpdateDatabaseException;
import com.github.souqly.souqly.model.Category;
import com.github.souqly.souqly.repository.rowmapper.CategoryRowMapper;

@Repository
public class CategoryRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String generateUUIDForCategory() {
		return UUID.randomUUID().toString();
	}

	public Category save(Category category) {
		String query = """
				    INSERT INTO categories (category_id, category_name, category_details, created_at, updated_at)
				    VALUES (?, ?, ?, ?, ?)
				""";
		String categoryId = generateUUIDForCategory();
		category.setCategoryId(categoryId);
		category.setCreatedAt(LocalDateTime.now());
		category.setUpdatedAt(LocalDateTime.now());
		int rows = jdbcTemplate.update(query, category.getCategoryId(), category.getCategoryName(),
				category.getCategoryDetails(), category.getCreatedAt(), category.getUpdatedAt());
		if (rows == 1) {
			return category;
		} else {
			throw new InsertDatabaseException("failed to inseret into table categories");
		}

	}

	public Category update(Category category) {
		String query = """
				    UPDATE categories
				    SET category_name = ?, category_details = ?, updated_at = ?
				    WHERE category_id = ?
				""";
		category.setUpdatedAt(LocalDateTime.now());
		int rows = jdbcTemplate.update(query, category.getCategoryName(), category.getCategoryDetails(),
				category.getUpdatedAt(), category.getCategoryId());
		if (rows == 1) {
			return category;
		} else {
			throw new UpdateDatabaseException("Cateogry");
		}
	}

	public boolean deleteCategory(String categoryId) {
		String query = """
				    DELETE FROM categories
				    WHERE category_id = ?
				""";
		int rows = jdbcTemplate.update(query, categoryId);
		if (rows == 1) {
			return true;
		} else if (rows == 0) {
			throw new DeleteDatabaseException("Category not found for ID: " + categoryId);
		} else {
			throw new DeleteDatabaseException(
					"Unexpected row count (" + rows + ") when deleting Category " + categoryId);
		}

	}

	public Category findById(String categoryId) {
		String query = "SELECT * FROM categories WHERE category_id = ?";
		try {
			Category category = jdbcTemplate.queryForObject(query, new CategoryRowMapper(), categoryId);
			return category;
		} catch (EmptyResultDataAccessException ex) {
			throw new FetchDatabaseException("Database error while fetching category with ID: " + categoryId);
		} catch (DataAccessException ex) {
			throw new FetchDatabaseException("Database error while fetching category with ID: " + categoryId);
		}
	}

	public List<Category> findPageCategories(int offset, int limit, String sortBy, String order) {
		String sql = String.format("""
					SELECT * FROM categories
					ORDER BY %s %s
					LIMIT ? OFFSET ?
				""", sortBy, order);

		return jdbcTemplate.query(sql, new CategoryRowMapper(), limit, offset);
	}

	public long countCategories() {
		String sql = "SELECT COUNT(*) FROM categories";
		return jdbcTemplate.queryForObject(sql, Long.class);
	}

	public List<Category> getAllGategories() {
		String sql = """
				    SELECT * FROM categories;
				""";
		return jdbcTemplate.query(sql, new CategoryRowMapper());
	}

}
