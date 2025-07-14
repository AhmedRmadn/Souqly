package com.github.souqly.souqly.security.service;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.stream.Collectors;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.souqly.souqly.model.User;


public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private String userId;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(String userId, String username, String email, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user, Set<String> roles) {
    	
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getPassword(),
                authorities);
    }
    
   

    public UserDetailsImpl() {

	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(userId, user.userId);
    }

	@Override
	public String toString() {
		return "UserDetailsImpl [userId=" + userId + ", username=" + username + ", email=" + email + ", password="
				+ password + ", authorities=" + authorities + "]";
	}
    
    

}