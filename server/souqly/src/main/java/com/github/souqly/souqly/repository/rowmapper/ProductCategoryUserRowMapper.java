package com.github.souqly.souqly.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.github.souqly.souqly.model.Category;
import com.github.souqly.souqly.model.Product;
import com.github.souqly.souqly.model.User;

public class ProductCategoryUserRowMapper implements RowMapper<Product> {

	@Override
	public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
		Product product = new Product();
		Category category = new Category();
		User user = new User();
		product.setProductId(rs.getString("p.product_id"));
		product.setProductName(rs.getString("p.product_name"));
		product.setImageUrl(rs.getString("p.image_url"));
		product.setProductDetails(rs.getString("p.product_details"));
		product.setQuantity(rs.getInt("p.quantity"));
		product.setPrice(rs.getDouble("p.price"));
		product.setDiscount(rs.getDouble("p.discount"));
		product.setSpecialPrice(rs.getDouble("p.special_price"));
		product.setCreatedAt(rs.getTimestamp("p.created_at").toLocalDateTime());
		product.setUpdatedAt(rs.getTimestamp("p.updated_at").toLocalDateTime());
		product.setCategoryId(rs.getString("p.category_id"));
		product.setSellerId(rs.getString("p.seller_id"));
		
		category.setCategoryId(rs.getString("c.category_id"));
		category.setCategoryName(rs.getString("c.category_name"));
		category.setCategoryDetails(rs.getString("c.category_details"));
		category.setCreatedAt(rs.getTimestamp("c.created_at").toLocalDateTime());
		category.setUpdatedAt(rs.getTimestamp("c.updated_at").toLocalDateTime());
		product.setProductCategory(category);
		
		user.setUserId(rs.getString("u.user_id"));
		user.setUserName(rs.getString("u.user_name"));
		user.setEmail(rs.getString("u.email"));
		user.setFirstName(rs.getString("u.first_name"));
		user.setLastName(rs.getString("u.last_name"));
		user.setCreatedAt(rs.getTimestamp("u.updated_at").toLocalDateTime());
		user.setUpdatedAt(rs.getTimestamp("u.updated_at").toLocalDateTime());
		product.setProductSeller(user);
		return product;
	}

}
