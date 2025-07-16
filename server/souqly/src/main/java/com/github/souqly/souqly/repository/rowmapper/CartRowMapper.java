package com.github.souqly.souqly.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.github.souqly.souqly.model.Cart;
import com.github.souqly.souqly.model.CartState;

public class CartRowMapper implements RowMapper<Cart>{

    @Override
    public Cart mapRow(ResultSet rs, int rowNum) throws SQLException {
        Cart cart = new Cart();
        cart.setCartId(rs.getString("cart_id"));
        cart.setCustomerId(rs.getString("customer_id"));

        // Safely convert string to enum
        cart.setCartState(CartState.valueOf(rs.getString("cart_state")));

        cart.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        cart.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return cart;
    }

}
