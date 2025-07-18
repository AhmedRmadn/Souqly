package com.github.souqly.souqly.repository.rowmapper;


import com.github.souqly.souqly.model.Payment;
import com.github.souqly.souqly.model.PaymentMethod;
import com.github.souqly.souqly.model.PaymentStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class PaymentRowMapper implements RowMapper<Payment> {

    @Override
    public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {
        Payment payment = new Payment();
        payment.setPaymentId(rs.getString("payment_id"));
        payment.setOrderId(rs.getString("order_id"));
        payment.setAmount(rs.getDouble("amount"));

        // Convert enum strings to enum constants
        payment.setStatus(PaymentStatus.valueOf(rs.getString("payment_state")));
        payment.setMethod(PaymentMethod.valueOf(rs.getString("method")));

        // Convert nullable timestamp
        Timestamp paymentTime = rs.getTimestamp("payment_time");
        if (paymentTime != null) {
            payment.setPaymentTime(paymentTime.toLocalDateTime());
        }

        payment.setTransactionId(rs.getString("transaction_id"));
        payment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        payment.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());

        return payment;
    }
}

