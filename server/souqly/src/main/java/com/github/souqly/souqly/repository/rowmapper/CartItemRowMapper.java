package com.github.souqly.souqly.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.github.souqly.souqly.model.CartItem;
import com.github.souqly.souqly.model.Product;

public class CartItemRowMapper implements RowMapper<CartItem> {

    @Override
    public CartItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        CartItem cartItem = new CartItem();

        cartItem.setCartItemId(rs.getString("ci.cart_item_id"));
        cartItem.setCartId(rs.getString("ci.cart_id"));
        cartItem.setProductId(rs.getString("ci.product_id"));
        cartItem.setPriceAtAddition(rs.getDouble("ci.price_at_addition"));
        cartItem.setDiscountAtAddition(rs.getDouble("ci.discount_at_addition"));
        cartItem.setSpecialPriceAtAddition(rs.getDouble("ci.special_price_at_addition"));
        cartItem.setQuantity(rs.getInt("ci.quantity"));
        cartItem.setCreatedAt(rs.getTimestamp("ci.created_at").toLocalDateTime());
        cartItem.setUpdatedAt(rs.getTimestamp("ci.updated_at").toLocalDateTime());

        // Map the joined product
        Product product = new Product();
        product.setProductId(rs.getString("p.product_id"));
        product.setProductName(rs.getString("p.product_name"));
        product.setImageUrl(rs.getString("p.image_url"));
        product.setProductDetails(rs.getString("p.product_details"));
        product.setQuantity(rs.getInt("p.quantity"));
        product.setPrice(rs.getDouble("p.price"));
        product.setDiscount(rs.getDouble("p.discount"));
        product.setSpecialPrice(rs.getDouble("p.special_price"));
        product.setCategoryId(rs.getString("p.category_id"));
        product.setSellerId(rs.getString("p.seller_id"));
        product.setCreatedAt(rs.getTimestamp("p.created_at").toLocalDateTime());
        product.setUpdatedAt(rs.getTimestamp("p.updated_at").toLocalDateTime());

        cartItem.setProduct(product);

        return cartItem;
    }
}
