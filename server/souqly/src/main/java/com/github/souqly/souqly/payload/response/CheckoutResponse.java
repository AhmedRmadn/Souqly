package com.github.souqly.souqly.payload.response;

public class CheckoutResponse {
	private OrderResponse orderResponse;
	private String paymentUrl;
	public OrderResponse getOrderResponse() {
		return orderResponse;
	}
	public void setOrderResponse(OrderResponse orderResponse) {
		this.orderResponse = orderResponse;
	}
	public String getPaymentUrl() {
		return paymentUrl;
	}
	public void setPaymentUrl(String paymentUrl) {
		this.paymentUrl = paymentUrl;
	}
	
	

}
