package com.github.souqly.souqly.security.controller;

import com.github.souqly.souqly.model.Role;
import com.github.souqly.souqly.model.RoleName;
import com.github.souqly.souqly.model.User;
import com.github.souqly.souqly.repository.RoleRepository;
import com.github.souqly.souqly.repository.UserRepository;
import com.github.souqly.souqly.repository.UserRoleRepository;
import com.github.souqly.souqly.security.jwt.JwtUtils;
import com.github.souqly.souqly.security.request.LoginRequest;
import com.github.souqly.souqly.security.request.SignupRequest;
import com.github.souqly.souqly.security.response.MessageResponse;
import com.github.souqly.souqly.security.response.UserInfoResponse;
import com.github.souqly.souqly.security.service.UserDetailsImpl;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	UserRoleRepository userRoleRepository;

	@Autowired
	PasswordEncoder encoder;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication;
		try {
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		} catch (AuthenticationException exception) {
			Map<String, Object> map = new HashMap<>();
			map.put("message", exception.getMessage());
			map.put("status", false);
			return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
		}

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		UserInfoResponse response = new UserInfoResponse(userDetails.getUserId(), userDetails.getUsername(), roles);

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(response);
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		System.out.println("signUp request");
		if (userRepository.existsByUserName(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User();
		user.setUserName(signUpRequest.getUsername());
		user.setEmail(signUpRequest.getEmail());
		user.setPassword(encoder.encode(signUpRequest.getPassword()));
		user.setFirstName(signUpRequest.getFirstName());
		user.setLastName(signUpRequest.getLastName());

		user = userRepository.save(user);

		Set<RoleName> setRoleNames = signUpRequest.getRole().stream().map(this::getRoleName)
				.collect(Collectors.toSet());

		if (setRoleNames == null || setRoleNames.isEmpty()) {
			Role userRole = roleRepository.findByName(RoleName.CUSTOMER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			userRoleRepository.assignRoleToUser(user.getUserId(), userRole.getRoleId());
		} else {
			for (RoleName roleName : setRoleNames) {
				Role role;
				switch (roleName) {
				case ADMIN:
					role = roleRepository.findByName(RoleName.ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					break;
				case SELLER:
					role = roleRepository.findByName(RoleName.SELLER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					break;
				default:
					role = roleRepository.findByName(RoleName.CUSTOMER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				}
				userRoleRepository.assignRoleToUser(user.getUserId(), role.getRoleId());
			}
		}

//		user.setRoles(roles);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	private RoleName getRoleName(String role) {
		switch (role.toUpperCase()) {
		case "SELLER": {
			return RoleName.SELLER;
		}
		case "ADMIN": {
			return RoleName.ADMIN;
		}
		default:
			return RoleName.CUSTOMER;
		}
	}

//	@GetMapping("/username")
//	public String currentUserName(Authentication authentication) {
//		System.out.println("user name");
//		if (authentication != null)
//			return authentication.getName();
//		else
//			return "";
//	}

	@GetMapping("/user")
	public ResponseEntity<?> getUserDetails(Authentication authentication) {
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		UserInfoResponse response = new UserInfoResponse(userDetails.getUserId(), userDetails.getUsername(), roles);

		return ResponseEntity.ok().body(response);
	}

	@PostMapping("/signout")
	public ResponseEntity<?> signoutUser() {
		ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
				.body(new MessageResponse("You've been signed out!"));
	}
}
