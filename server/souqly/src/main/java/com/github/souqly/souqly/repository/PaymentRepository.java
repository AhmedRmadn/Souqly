package com.github.souqly.souqly.repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.souqly.souqly.Exception.FetchDatabaseException;
import com.github.souqly.souqly.Exception.UpdateDatabaseException;
import com.github.souqly.souqly.model.Payment;
import com.github.souqly.souqly.model.PaymentStatus;
import com.github.souqly.souqly.repository.rowmapper.PaymentRowMapper;

@Repository
public class PaymentRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	private String generateUUIDForPayment() {
		return UUID.randomUUID().toString();
	}

	public Payment createPayment(Payment payment) {
		payment.setPaymentId(generateUUIDForPayment());
		LocalDateTime currentMoment = LocalDateTime.now();
		payment.setCreatedAt(currentMoment);
		payment.setUpdatedAt(currentMoment);

		String query = """
				    INSERT INTO payments (
				        payment_id,
				        order_id,
				        amount,
				        payment_state,
				        method,
				        payment_time,
				        transaction_id,
				        created_at,
				        updated_at
				    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
				""";

		int rows = jdbcTemplate.update(query, payment.getPaymentId(), payment.getOrderId(), payment.getAmount(),
				payment.getStatus().name(), payment.getMethod().name(), payment.getPaymentTime(), // should be
																									// java.sql.Timestamp
				payment.getTransactionId(), Timestamp.valueOf(payment.getCreatedAt()),
				Timestamp.valueOf(payment.getUpdatedAt()));

		if (rows != 1) {
			throw new FetchDatabaseException("Failed to create payment with ID " + payment.getPaymentId());
		}

		return payment;
	}

	public void updateOrderState(String paymentId, PaymentStatus paymentState) {
		String query = """
				UPDATE payments SET
				payment_state = ?,
				updated_at = ?,
				payment_time = ?
				WHERE payment_id = ?
				""";
		LocalDateTime current = LocalDateTime.now();
		int row = jdbcTemplate.update(query, paymentState.name(), current, current, paymentId);
		if (row != 1)
			throw new UpdateDatabaseException("could update payment state to " + paymentState.name());
	}

	public Payment getPaymentByTransactionId(String transactionId) {
		String query = """
				SELECT * FROM payments
				WHERE transaction_id = ?;
				""";
		try {
			Payment payment = jdbcTemplate.queryForObject(query, new PaymentRowMapper(), transactionId);
			return payment;
		} catch (DataAccessException e) {
			throw new FetchDatabaseException("could not fetch payment with transactionId " + transactionId);
		}
	}

}
