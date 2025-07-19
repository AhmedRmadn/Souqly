package com.github.souqly.souqly.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.souqly.souqly.Exception.InsertDatabaseException;
import com.github.souqly.souqly.model.OrderItem;
import com.github.souqly.souqly.repository.rowmapper.OrderItemRowMapper;

@Repository
public class OrderItemRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String generateUUIDForOrderItem() {
		return UUID.randomUUID().toString();
	}

	public OrderItem createOrderItem(OrderItem orderItem) {
		String query = """
					INSERT INTO order_items (
						order_item_id,
						order_id,
						product_id,
						product_name,
						product_image_url,
						product_details,
						quantity,
						price,
						discount,
						special_price,
						seller_id,
						seller_email,
						created_at,
						updated_at
					) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
				""";

		LocalDateTime now = LocalDateTime.now();
		orderItem.setOrderItemId(generateUUIDForOrderItem());
		orderItem.setCreatedAt(now);
		orderItem.setUpdatedAt(now);

		int rows = jdbcTemplate.update(query, orderItem.getOrderItemId(), orderItem.getOrderId(),
				orderItem.getProductId(), orderItem.getProductName(), orderItem.getProductImageUrl(),
				orderItem.getProductDetails(), orderItem.getQuantity(), orderItem.getPrice(), orderItem.getDiscount(),
				orderItem.getSpecialPrice(), orderItem.getSellerId(), orderItem.getSellerEmail(),
				orderItem.getCreatedAt(), orderItem.getUpdatedAt());

		if (rows != 1) {
			throw new InsertDatabaseException("Could not create order item for order_id " + orderItem.getOrderId());
		}

		return orderItem;
	}


	public List<OrderItem> orderItemsForOrder(String orderId) {
	    String query = """
	        SELECT * FROM order_items
	        WHERE order_id = ?
	    """;
	    return jdbcTemplate.query(query, new OrderItemRowMapper(), orderId);
	}

}
