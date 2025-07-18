package com.github.souqly.souqly.controller;

import com.github.souqly.souqly.service.CheckoutService;
import com.google.gson.JsonSyntaxException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/stripe")
public class PaymentController {
	  @Autowired 
	    private CheckoutService checkoutService;

	    @Value("${stripe.webhook.secret}")
	    private String webhookSecret;


	    @PostMapping("/webhook")
	    public ResponseEntity<String> webhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
	        Event event;
	        try {
	            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
	        } catch (JsonSyntaxException e) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid payload");
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
	        }
	        Session session = (Session) event.getData().getObject();
	        switch (event.getType()) {
	            case "checkout.session.completed":
	                System.out.println("Payment for session " + session.getId() + " succeeded!");
				String userId = session.getMetadata().get("userId");
				String orderId = session.getMetadata().get("orderId");
				System.out.println("Payment complete. User ID: " + userId + ", Cart ID: " + orderId);
				checkoutService.paymentSuccessed(session.getId());
	                break;
	            default:
	                System.out.println("Unhandled event type: " + event.getType());
	                checkoutService.paymentFailed(session.getId());
	        }

	        return ResponseEntity.ok().build();
	    }
}
