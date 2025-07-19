package com.github.souqly.souqly.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.souqly.souqly.Exception.ApiException;
import com.github.souqly.souqly.model.CartState;
import com.github.souqly.souqly.model.Order;
import com.github.souqly.souqly.model.OrderItem;
import com.github.souqly.souqly.model.OrderStatus;
import com.github.souqly.souqly.model.Payment;
import com.github.souqly.souqly.model.PaymentStatus;
import com.github.souqly.souqly.payload.request.CreateOrderDTO;
import com.github.souqly.souqly.payload.request.CreateOrderItemDTO;
import com.github.souqly.souqly.payload.request.CreateOrderRequest;
import com.github.souqly.souqly.payload.request.PaymentDTO;
import com.github.souqly.souqly.payload.response.AddressResponse;
import com.github.souqly.souqly.payload.response.CartItemResponse;
import com.github.souqly.souqly.payload.response.CartResponse;
import com.github.souqly.souqly.payload.response.CheckoutResponse;
import com.github.souqly.souqly.payload.response.OrderItemResponse;
import com.github.souqly.souqly.payload.response.OrderResponse;
import com.github.souqly.souqly.payload.response.ProductResponse;
import com.github.souqly.souqly.repository.UserRepository;
import com.stripe.exception.StripeException;

@Service
public class CheckoutService {

	@Autowired
	CartService cartService;

	@Autowired
	ProductService productService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	AddressService addressService;

	@Autowired
	OrderService orderService;

	@Autowired
	OrderItemService orderItemService;

	@Autowired
	PaymentService paymentService;

	@Autowired
	Mapper mapper;

	@Transactional
	public CheckoutResponse checkout(CreateOrderRequest createOrderRequest, String userId) throws StripeException {
//		System.out.println("enter "+createOrderRequest.getNum());
		OrderResponse orderResponse = createOrder(createOrderRequest, userId);
//		System.out.println("order-end "+createOrderRequest.getNum());
		String paymentUrl = performPayment(orderResponse, userId);
		
		CheckoutResponse checkoutResponse = new CheckoutResponse();
		checkoutResponse.setOrderResponse(orderResponse);
		checkoutResponse.setPaymentUrl(paymentUrl);
//		System.out.println("out "+createOrderRequest.getNum());
		return checkoutResponse;
	}

	private String performPayment(OrderResponse orderResponse, String userId) throws StripeException {
		PaymentDTO paymentDTO = new PaymentDTO();
		paymentDTO.setUserId(userId);
		paymentDTO.setOrderId(orderResponse.getOrderId());
		paymentDTO.setAmount(orderResponse.getTotalAmount());
		paymentDTO.setQuantity(1l);
		return paymentService.createPayment(paymentDTO);
	}


	private OrderResponse createOrder(CreateOrderRequest createOrderRequest, String userId) {
//		System.out.println("order 1 "+createOrderRequest.getNum());
//		CartResponse cartResponse = cartService.viewUserCart(userId);
		CartResponse cartResponse = cartService.getUserCartForCheckout(userId);
		if (cartResponse.getTotalItems() == 0) {
			throw new ApiException("your cart is Empty", HttpStatus.BAD_REQUEST);
		}
		String userAddress = createOrderRequest.getAddressId();
		Order order = createOrderRecord(userAddress, cartResponse, userId);

		List<OrderItem> orderItems = new ArrayList<>();
//		System.out.println("order 2 "+createOrderRequest.getNum());
		for (CartItemResponse item : cartResponse.getItems()) {
			ProductResponse product = item.getProductResponse();
			if (product.getQuantity() < item.getQuantity()) {
				throw new ApiException("product out of stock " + product.getProductId(), HttpStatus.BAD_REQUEST);
			}
			productService.updateProductQuantityForCheckout(product.getProductId(), -item.getQuantity());
			OrderItem orderItem = createOrderItem(item, order);
			orderItems.add(orderItem);
		}
//		System.out.println("order 3 "+createOrderRequest.getNum());
		List<OrderItemResponse> orderItemResponses = orderItems.stream().map(mapper::mapOrderItem).toList();
		cartService.updateCartState(CartState.CHECKED_OUT,CartState.ACTIVE, cartResponse.getCartId());
		try {
			Thread.sleep(createOrderRequest.getNum());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mapper.mapOrdertoOrderResponse(order, orderItemResponses);
	}

	private Order createOrderRecord(String addressId, CartResponse cartResponse, String userId) {
		AddressResponse addressResponse = addressService.findAddressById(addressId, userId);
		String userEmail = userRepository.findUserEmail(userId);
		CreateOrderDTO createOrderDTO = new CreateOrderDTO();
		createOrderDTO.setUserId(userId);
		createOrderDTO.setUserEmail(userEmail);
		createOrderDTO.setStatus(OrderStatus.PENDING);
		createOrderDTO.setAddress(addressResponse.toString());
		createOrderDTO.setTotalAmount(cartResponse.getCurrentTotalPrice());
		return orderService.createOrder(createOrderDTO);

	}

	private OrderItem createOrderItem(CartItemResponse item, Order order) {
		String sellerEmail = userRepository.findUserEmail(item.getProductResponse().getSellerId());
		CreateOrderItemDTO createOrderItemDTO = new CreateOrderItemDTO();
		createOrderItemDTO.setOrderId(order.getOrderId());
		createOrderItemDTO.setProductId(item.getProductId());
		createOrderItemDTO.setProductName(item.getProductResponse().getProductName());
		createOrderItemDTO.setProductImageUrl(item.getProductResponse().getImageUrl());
		createOrderItemDTO.setProductDetails(item.getProductResponse().getProductDetails());
		createOrderItemDTO.setQuantity(item.getQuantity());
		createOrderItemDTO.setPrice(item.getCurrentPrice());
		createOrderItemDTO.setDiscount(item.getCurrentDiscount());
		createOrderItemDTO.setSpecialPrice(item.getCurrentSpecialPrice());
		createOrderItemDTO.setSellerId(item.getProductResponse().getSellerId());
		createOrderItemDTO.setSellerEmail(sellerEmail);
		return orderItemService.createOrderItem(createOrderItemDTO);
	}

	@Transactional
	public void paymentSuccessed(String transactionId) {
		Payment payment = paymentService.getPaymentByTransactionId(transactionId);
		paymentService.updateOrderState(payment.getPaymentId(), PaymentStatus.SUCCESS);
		orderService.updateOrderState(payment.getOrderId(), OrderStatus.CONFIRMED);
	}

	@Transactional
	public void paymentFailed(String transactionId) {
		Payment payment = paymentService.getPaymentByTransactionId(transactionId);
		paymentService.updateOrderState(payment.getPaymentId(), PaymentStatus.FAILED);
		List<OrderItem> orderItems = orderItemService.orderItemsForOrder(payment.getOrderId());
		for (OrderItem orderItem : orderItems) {
			productService.updateProductQuantityForCheckout(orderItem.getProductId(), orderItem.getQuantity());
		}
		orderService.updateOrderState(payment.getOrderId(), OrderStatus.CANCELLED);
	}

}
