package com.github.souqly.souqly.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.souqly.souqly.Exception.DeleteDatabaseException;
import com.github.souqly.souqly.Exception.FetchDatabaseException;
import com.github.souqly.souqly.Exception.InsertDatabaseException;
import com.github.souqly.souqly.Exception.UpdateDatabaseException;
import com.github.souqly.souqly.model.Cart;
import com.github.souqly.souqly.model.CartItem;
import com.github.souqly.souqly.repository.rowmapper.CartItemRowMapper;

@Repository
public class CartItemRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	private String generateUUIDForCartItem() {
		return UUID.randomUUID().toString();
	}

	public CartItem save(CartItem cartItem) {
		String query = """
				    INSERT INTO cart_items (
				        cart_item_id,
				        cart_id,
				        product_id,
				        price_at_addition,
				        discount_at_addition,
				        special_price_at_addition,
				        quantity,
				        created_at,
				        updated_at
				    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
				""";

		cartItem.setCartItemId(generateUUIDForCartItem());
		cartItem.setCreatedAt(LocalDateTime.now());
		cartItem.setUpdatedAt(LocalDateTime.now());

		int row = jdbcTemplate.update(query, cartItem.getCartItemId(), cartItem.getCartId(), cartItem.getProductId(),
				cartItem.getPriceAtAddition(), cartItem.getDiscountAtAddition(), cartItem.getSpecialPriceAtAddition(),
				cartItem.getQuantity(), cartItem.getCreatedAt(), cartItem.getUpdatedAt());
		if (row != 1)
			throw new InsertDatabaseException("failed to inseret into table cartItems");

		return cartItem;
	}

	public boolean existsProductInCart(String productId, String cartId) {
		String sql = """
				    SELECT COUNT(*) FROM cart_items
				    WHERE cart_id = ? AND product_id = ?
				""";

		Integer count = jdbcTemplate.queryForObject(sql, Integer.class, cartId, productId);
		return count != null && count > 0;
	}

	public List<CartItem> getCartItemsInCart(String cartId) {
		String query = """
				SELECT * FROM
				cart_items ci JOIN
				products p ON
				ci.product_id = p.product_id
				WHERE ci.cart_id = ?;
				""";
		List<CartItem> cartItems = jdbcTemplate.query(query, new CartItemRowMapper(), cartId);
		return cartItems;
	}

	public String getCartId(String cartItemId) {
		String query = """
				SELECT cart_id FROM
				cart_items
				WHERE cart_item_id = ?
				""";
		try {
			String cartId = jdbcTemplate.queryForObject(query, String.class, cartItemId);
			return cartId;
		} catch (DataAccessException e) {
			throw new FetchDatabaseException("could not find the target cartItem");
		}
	}

	public void delete(String cartItemId) {
		String query = """
				DELETE FROM cart_items
				WHERE cart_item_id = ?
				""";
		int row = jdbcTemplate.update(query, cartItemId);
		if (row != 1)
			throw new DeleteDatabaseException("could not delete from cart_items id: " + cartItemId);

	}

	public CartItem getCartItemById(String cartItemId) {
		String query = """
				SELECT * FROM
				cart_items ci JOIN
				products p ON
				ci.product_id = p.product_id
				WHERE ci.cart_item_id = ?;
				""";
		try {
			CartItem cartItem = jdbcTemplate.queryForObject(query, new CartItemRowMapper(), cartItemId);
			return cartItem;
		} catch (DataAccessException e) {
			throw new FetchDatabaseException("could not find the target cartItem");
		}
	}

	public CartItem updateQuantity(CartItem cartItem) {
		String query = """
				UPDATE cart_items
				SET quantity = ?,
				updated_at = ?
				WHERE cart_item_id = ?
				""";
		cartItem.setUpdatedAt(LocalDateTime.now());
		int row = jdbcTemplate.update(query, cartItem.getQuantity(), cartItem.getUpdatedAt(), cartItem.getCartItemId());
		if (row != 1)
			throw new UpdateDatabaseException(
					"error while updating quantity for cart item with id " + cartItem.getCartItemId());
		return cartItem;
	}
	
	public void updateQuantitySafely(String cartItemId, int delta) {
	    String query = """
	        UPDATE cart_items AS ci
	        SET quantity = ci.quantity + ?, updated_at = ?
	        FROM products p
	        WHERE ci.cart_item_id = ?
	          AND ci.product_id = p.product_id
	          AND (ci.quantity + ?) <= p.quantity
	          AND (ci.quantity + ?) >= 0
	    """;

	    int affected = jdbcTemplate.update(query,
	        delta,
	        LocalDateTime.now(),
	        cartItemId,
	        delta,
	        delta
	    );

	    if (affected != 1) {
	    	throw new UpdateDatabaseException(
					"error while updating quantity for cart item with id " + cartItemId);
	    }
	}

	public Cart getUserCartForCheckOut(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
