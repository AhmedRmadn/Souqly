package com.github.souqly.souqly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.github.souqly.souqly.Exception.ApiException;
import com.github.souqly.souqly.Exception.UnAuthorizedException;
import com.github.souqly.souqly.model.Cart;
import com.github.souqly.souqly.model.CartItem;
import com.github.souqly.souqly.model.CartState;
import com.github.souqly.souqly.model.Product;
import com.github.souqly.souqly.payload.request.AddCartProductRequest;
import com.github.souqly.souqly.payload.request.UpdateCartItemQuantityRequest;
import com.github.souqly.souqly.payload.response.CartItemResponse;
import com.github.souqly.souqly.payload.response.CartResponse;
import com.github.souqly.souqly.repository.CartItemRepository;
import com.github.souqly.souqly.repository.CartRepository;
import com.github.souqly.souqly.repository.ProductRepository;

import jakarta.validation.constraints.Pattern;

@Service
public class CartService {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	CartItemRepository cartItemRepository;

	@Autowired
	Mapper mapper;

	@Transactional
	public CartItemResponse addCartProduct(AddCartProductRequest addCartProductRequest, String activeUserId) {
		/*
		 * fecth product user cart validate product exists fetch product validate
		 * quantity create cart item
		 */
		String productId = addCartProductRequest.getProductId();
		int quantity = addCartProductRequest.getQuantity();

		Product product = productRepository.findProductById(productId);

		if (product.getQuantity() < quantity) {
			throw new ApiException("number of available products less than requied", HttpStatus.BAD_REQUEST);
		}

		Cart userCart = getUserCart(activeUserId);

		boolean thisProductexistsInThatCart = cartItemRepository.existsProductInCart(product.getProductId(),
				userCart.getCartId());
		if (thisProductexistsInThatCart) {
			throw new ApiException("this product already in  your cart you can update that from cart section",
					HttpStatus.BAD_REQUEST);
		}

		CartItem cartItem = createCartItem(userCart, product, quantity);

		return mapCartItem(cartItem);
	}

	private CartItem createCartItem(Cart cart, Product product, int quantity) {
		CartItem cartItem = new CartItem();
		cartItem.setCartId(cart.getCartId());
		cartItem.setProductId(product.getProductId());
		cartItem.setPriceAtAddition(product.getPrice());
		cartItem.setSpecialPriceAtAddition(product.getSpecialPrice());
		cartItem.setDiscountAtAddition(product.getDiscount());
		cartItem.setQuantity(quantity);
		cartItem.setProduct(product);
		cartItem = cartItemRepository.save(cartItem);
		return cartItem;

	}

	private Cart getUserCart(String userId) {
		Cart cart = cartRepository.findUserActiveCart(userId);
		if (cart == null) {
			cart = new Cart();
			cart.setCustomerId(userId);
			cart.setCartState(CartState.ACTIVE);
			cart = cartRepository.createCart(cart);
		}
		return cart;

	}

	private CartItemResponse mapCartItem(CartItem cartItem) {
		double currentPrice = cartItem.getProduct().getPrice();
		double currentDiscount = cartItem.getProduct().getDiscount();
		double currentSpecialPrice = cartItem.getProduct().getSpecialPrice();
		boolean availableForRequiredQuantity = cartItem.getQuantity() <= cartItem.getProduct().getQuantity();
		boolean priceChangedSinceAdded = cartItem.getSpecialPriceAtAddition() != cartItem.getProduct()
				.getSpecialPrice();
		return mapper.mapCartItem(cartItem, currentPrice, currentDiscount, currentSpecialPrice,
				availableForRequiredQuantity, priceChangedSinceAdded);
	}

	public CartResponse viewUserCart(String userId) {
		Cart userCart = getUserCart(userId);
		List<CartItem> cartItems = cartItemRepository.getCartItemsInCart(userCart.getCartId());
		List<CartItemResponse> cartItemResponses = cartItems.stream().map(this::mapCartItem).toList();
		return mapCartToResponse(userCart, cartItemResponses);
	}

	//// mapCartToResponse(Cart cart, List<CartItemResponse> items, long totalItems,
	//// double totalPriceAtAddition, double currentTotalPrice)
	private CartResponse mapCartToResponse(Cart cart, List<CartItemResponse> cartItemResponses) {
		double totalPriceAtAddition = 0;
		double currentTotalPrice = 0;

		for (CartItemResponse cartItemResponse : cartItemResponses) {
			int quantity = cartItemResponse.getQuantity();
			totalPriceAtAddition += cartItemResponse.getSpecialPriceAtAddition() * quantity;
			currentTotalPrice += cartItemResponse.getCurrentSpecialPrice() * quantity;
		}

		long totalItems = cartItemResponses.size(); // still fine — number of distinct items
		return mapper.mapCartToResponse(cart, cartItemResponses, totalItems, totalPriceAtAddition, currentTotalPrice);
	}

	@Transactional
	public void removeCartItemFromCart(String cartItemId, String userId) {
		Cart userCart = getUserCart(userId);
		String cartIdForTargetItem = cartItemRepository.getCartId(cartItemId);
		if (!userCart.getCartId().equals(cartIdForTargetItem)) {
			throw new UnAuthorizedException("you do not have the permission to delete that item");
		}
		cartItemRepository.delete(cartItemId);
	}

	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public CartItemResponse updateCartItemInCart(UpdateCartItemQuantityRequest updateCartItemQuantityRequest,
			String cartItemId, String userId) {
		int delta = updateCartItemQuantityRequest.getDelta();
		Cart userCart = getUserCart(userId);
		CartItem cartItem = cartItemRepository.getCartItemById(cartItemId);
		if (!userCart.getCartId().equals(cartItem.getCartId())) {
			throw new UnAuthorizedException("you do not have the permission to delete that item");
		}
		cartItem.setQuantity(cartItem.getQuantity() + delta);
		if (cartItem.getQuantity() == 0) {
			cartItemRepository.delete(cartItemId);
			return null;
		} else if (cartItem.getQuantity() < 0) {
			throw new ApiException("quantity should be greater than 0", HttpStatus.BAD_REQUEST);
		} else if (cartItem.getQuantity() > cartItem.getProduct().getPrice()) {
			throw new ApiException("number of available products less than requied", HttpStatus.BAD_REQUEST);
		}

		cartItemRepository.updateQuantitySafely(cartItemId, delta);

		return mapCartItem(cartItem);
	}

	public void updateCartState(CartState newCartState, CartState currentCartState, String CartId) {
		cartRepository.updateCartState(newCartState,currentCartState, CartId);
	}

	public CartResponse getUserCartForCheckout(String userId) {
		Cart userCart = cartRepository.getUserCartForCheckOut(userId);
		List<CartItem> cartItems = cartItemRepository.getCartItemsInCart(userCart.getCartId());
		List<CartItemResponse> cartItemResponses = cartItems.stream().map(this::mapCartItem).toList();
		return mapCartToResponse(userCart, cartItemResponses);
	}

}
