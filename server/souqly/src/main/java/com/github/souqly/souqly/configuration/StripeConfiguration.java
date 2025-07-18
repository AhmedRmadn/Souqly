package com.github.souqly.souqly.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.stripe.Stripe;

import jakarta.annotation.PostConstruct;

@Configuration
public class StripeConfiguration {
	@Value("${stripe.api.key}")
	private String stripeApiKey;

	@PostConstruct
	public void init() {
		Stripe.apiKey = stripeApiKey;
	}

}
