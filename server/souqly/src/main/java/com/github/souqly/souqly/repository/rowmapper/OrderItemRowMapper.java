package com.github.souqly.souqly.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.springframework.jdbc.core.RowMapper;

import com.github.souqly.souqly.model.OrderItem;

public class OrderItemRowMapper implements RowMapper<OrderItem> {

    @Override
    public OrderItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemId(rs.getString("order_item_id"));
        orderItem.setOrderId(rs.getString("order_id"));
        orderItem.setProductId(rs.getString("product_id"));
        orderItem.setProductName(rs.getString("product_name"));
        orderItem.setProductImageUrl(rs.getString("product_image_url"));
        orderItem.setProductDetails(rs.getString("product_details"));
        orderItem.setQuantity(rs.getInt("quantity"));
        orderItem.setPrice(rs.getDouble("price"));
        orderItem.setDiscount(rs.getDouble("discount"));
        orderItem.setSpecialPrice(rs.getDouble("special_price"));
        orderItem.setSellerId(rs.getString("seller_id"));
        orderItem.setSellerEmail(rs.getString("seller_email"));
        orderItem.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
        orderItem.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));
        return orderItem;
    }
}
