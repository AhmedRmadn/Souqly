package com.github.souqly.souqly.security.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 255, message = "Password must be at least 8 characters long")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
