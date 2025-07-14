package com.github.souqly.souqly.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.github.souqly.souqly.model.Category;

public class CategoryRowMapper implements RowMapper<Category> {

	@Override
	public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
		Category category = new Category();
		category.setCategoryId(rs.getString("category_id"));
		category.setCategoryName(rs.getString("category_name"));
		category.setCategoryDetails(rs.getString("category_details"));
		category.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
		category.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
		return category;
	}

}
