package com.github.souqly.souqly.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.souqly.souqly.model.Payment;
import com.github.souqly.souqly.model.PaymentMethod;
import com.github.souqly.souqly.model.PaymentStatus;
import com.github.souqly.souqly.payload.request.PaymentDTO;
import com.github.souqly.souqly.repository.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class PaymentService {

	@Autowired
	PaymentRepository paymentRepository;

	private Session createCheckoutSession(PaymentDTO request) throws StripeException {
		String YOUR_DOMAIN = "http://localhost:4242";

		var productData = SessionCreateParams.LineItem.PriceData.ProductData.builder().setName("Souqly").build();

		var priceData = SessionCreateParams.LineItem.PriceData.builder().setCurrency("usd")
				.setUnitAmount((long) (request.getAmount() * 100)).setProductData(productData).build();

		var lineItem = SessionCreateParams.LineItem.builder().setQuantity(request.getQuantity()).setPriceData(priceData)
				.build();

		var params = SessionCreateParams.builder().setMode(SessionCreateParams.Mode.PAYMENT)
				.setSuccessUrl(YOUR_DOMAIN + "/success.html").setCancelUrl(YOUR_DOMAIN + "/cancel.html")
				.addLineItem(lineItem).putMetadata("userId", request.getUserId())
				.putMetadata("orderId", request.getOrderId()).build();
		Session session = Session.create(params);
		return session;
	}

	@Transactional
	public String createPayment(PaymentDTO request) throws StripeException {
		Session session = createCheckoutSession(request);
		String transactionId = session.getId();
		Payment payment = createPaymentRecord(request, transactionId);
		return session.getUrl();
	}

	private Payment createPaymentRecord(PaymentDTO request, String transactionId) {
		Payment payment = new Payment();
		payment.setOrderId(request.getOrderId());
		payment.setAmount(request.getAmount());
		payment.setStatus(PaymentStatus.PENDING);
		payment.setMethod(PaymentMethod.STRIPE);
		payment.setTransactionId(transactionId);
		payment = paymentRepository.createPayment(payment);
		return payment;
	}

	public void updateOrderState(String paymentId, PaymentStatus paymentState) {
		paymentRepository.updateOrderState(paymentId, paymentState);
	}

	public Payment getPaymentByTransactionId(String transactionId) {
		return paymentRepository.getPaymentByTransactionId(transactionId);
	}

}
