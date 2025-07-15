package com.github.souqly.souqly.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.github.souqly.souqly.model.Category;
import com.github.souqly.souqly.model.Product;
import com.github.souqly.souqly.model.User;

public class ProductRowMapper implements RowMapper<Product>{

	@Override
	public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
		Product product = new Product();
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
		return product;
	}

}
