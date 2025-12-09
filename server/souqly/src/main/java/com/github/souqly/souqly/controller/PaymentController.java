package com.github.souqly.souqly.controller;

import com.github.souqly.souqly.service.CheckoutService;
import com.google.gson.JsonSyntaxException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        // ❌ REMOVED THE GLOBAL CAST FROM HERE

        switch (event.getType()) {
            case "checkout.session.completed":
                // ✅ SAFE CAST: We know this event contains a Session
                Session session = (Session) event.getData().getObject();
                
                System.out.println("Payment for session " + session.getId() + " succeeded!");
                // Null checks for metadata are good practice
                String userId = (session.getMetadata() != null) ? session.getMetadata().get("userId") : "Unknown";
                String orderId = (session.getMetadata() != null) ? session.getMetadata().get("orderId") : "Unknown";
                
                System.out.println("User ID: " + userId + ", Order ID: " + orderId);
                checkoutService.paymentSuccessed(session.getId());
                break;

            // Handle specific failures if you want (e.g., payment failed)
            case "checkout.session.expired":
            case "checkout.session.async_payment_failed":
                Session failedSession = (Session) event.getData().getObject();
                checkoutService.paymentFailed(failedSession.getId());
                break;

            default:
                // ⚠️ IMPORTANT: Do NOT call paymentFailed() here!
                // Stripe sends many informational events (like 'charge.succeeded').
                // If you fail the order on 'default', you will accidentally cancel valid orders.
                System.out.println("Unhandled event type: " + event.getType());
                break;
        }

        return ResponseEntity.ok().build();
    }
}