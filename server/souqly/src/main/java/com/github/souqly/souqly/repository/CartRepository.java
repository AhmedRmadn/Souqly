package com.github.souqly.souqly.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.souqly.souqly.Exception.InsertDatabaseException;
import com.github.souqly.souqly.model.Cart;
import com.github.souqly.souqly.repository.rowmapper.CartRowMapper;

@Repository
public class CartRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	private String generateUUIDForCart() {
		return UUID.randomUUID().toString();
	}

	public Cart findUserActiveCart(String userId) {
		String sql = """
				    SELECT * FROM carts
				    WHERE customer_id = ? AND cart_state = 'ACTIVE'
				    LIMIT 1
				""";

		List<Cart> result = jdbcTemplate.query(sql, new CartRowMapper(), userId);
		return result.isEmpty() ? null : result.get(0);
	}

	public Cart createCart(Cart cart) {
	    String query = """
	        INSERT INTO carts (
	            cart_id,
	            customer_id,
	            cart_state,
	            created_at,
	            updated_at
	        ) VALUES (?, ?, ?, ?, ?)
	    """;

	    cart.setCartId(generateUUIDForCart());
	    cart.setCreatedAt(LocalDateTime.now());
	    cart.setUpdatedAt(LocalDateTime.now());

	    int row = jdbcTemplate.update(
	        query,
	        cart.getCartId(),
	        cart.getCustomerId(),
	        cart.getCartState().name(), // Assuming enum CartState
	        cart.getCreatedAt(),
	        cart.getUpdatedAt()
	    );
		if (row != 1)
			throw new InsertDatabaseException("failed to inseret into table carts");

	    return cart;
	}

}
