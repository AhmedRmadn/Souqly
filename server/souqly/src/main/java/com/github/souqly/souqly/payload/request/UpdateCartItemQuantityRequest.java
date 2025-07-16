package com.github.souqly.souqly.payload.request;

import jakarta.validation.constraints.NotNull;

public class UpdateCartItemQuantityRequest {
	
	@NotNull
	private Integer delta;

	public Integer getDelta() {
		return delta;
	}

	public void setDelta(Integer delta) {
		this.delta = delta;
	}
	
	

}
