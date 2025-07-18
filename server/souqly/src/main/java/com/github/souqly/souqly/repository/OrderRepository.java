package com.github.souqly.souqly.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.souqly.souqly.Exception.InsertDatabaseException;
import com.github.souqly.souqly.Exception.UpdateDatabaseException;
import com.github.souqly.souqly.model.Order;
import com.github.souqly.souqly.model.OrderStatus;

@Repository
public class OrderRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String generateUUIDForOrder() {
		return UUID.randomUUID().toString();
	}

	public Order createOrder(Order order) {
		String query = """
					INSERT INTO orders (
						order_id,
						user_id,
						user_email,
						address,
						status,
						total_amount,
						created_at,
						updated_at
					) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
				""";

		LocalDateTime now = LocalDateTime.now();
		order.setOrderId(generateUUIDForOrder());
		order.setCreatedAt(now);
		order.setUpdatedAt(now);

		if (order.getStatus() == null) {
			order.setStatus(OrderStatus.PENDING);
		}

		int row = jdbcTemplate.update(query, order.getOrderId(), order.getUserId(), order.getUserEmail(),
				order.getAddress(), order.getStatus().name(), // enum to string
				order.getTotalAmount(), order.getCreatedAt(), order.getUpdatedAt());

		if (row != 1) {
			throw new InsertDatabaseException("Could not create a new order");
		}

		return order;
	}

	public void updateOrderState(String orderId, OrderStatus orderStatus) {
		String query = """
				UPDATE orders SET
				status = ?,
				updated_at = ?
				WHERE order_id = ?
				""";
		int row = jdbcTemplate.update(query, orderStatus.name(), LocalDateTime.now(), orderId);
		if (row != 1)
			throw new UpdateDatabaseException("could update order state to " + orderStatus.name());
	}
}
