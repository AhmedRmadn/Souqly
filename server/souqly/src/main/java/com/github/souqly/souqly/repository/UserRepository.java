package com.github.souqly.souqly.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.souqly.souqly.Exception.InsertionDatabaseException;
import com.github.souqly.souqly.model.User;
import com.github.souqly.souqly.repository.rowmapper.UserRowMapper;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public String generateUUIDForUser() {
		return UUID.randomUUID().toString();
	}

	public User save(User user) {
		String sql = """
				    INSERT INTO users (user_id, first_name, last_name, user_name, email, password, created_at, updated_at)
				    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
				""";

		user.setUserId(generateUUIDForUser());
		user.setCreatedAt(LocalDateTime.now());
		user.setUpdatedAt(LocalDateTime.now());

		int rows = jdbcTemplate.update(sql, user.getUserId(), user.getFirstName(), user.getLastName(),
				user.getUserName(), user.getEmail(), user.getPassword(), user.getCreatedAt(), user.getUpdatedAt());
		if (rows == 1) {
			return user;
		} else {
			throw new InsertionDatabaseException("User");
		}
	}

	public Optional<User> findById(String userId) {
		String sql = "SELECT * FROM users WHERE user_id = ?";
		try {
			User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), userId);
			return Optional.of(user);
		} catch (EmptyResultDataAccessException ex) {
			return Optional.empty();
		} catch (DataAccessException ex) {
			throw new RuntimeException("Error fetching user by ID: " + userId, ex);
		}
	}

	public Optional<User> findByUserName(String userName) {
		String sql = "SELECT * FROM users WHERE user_name = ?";
		try {
			User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), userName);
			return Optional.of(user);
		} catch (EmptyResultDataAccessException ex) {
			return Optional.empty();
		} catch (DataAccessException ex) {
			throw new RuntimeException("Error fetching user by username: " + userName, ex);
		}
	}

	public boolean existsByUserName(String userName) {
		String sql = "SELECT * FROM users WHERE user_name = ?";
		try {
			User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), userName);
			return true;
		} catch (EmptyResultDataAccessException ex) {
			return false;
		} catch (DataAccessException ex) {
			throw new RuntimeException("Error fetching user by username: " + userName, ex);

		}
	}

	public boolean existsByEmail(String userEmail) {
		String sql = "SELECT * FROM users WHERE email = ?";
		try {
			User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), userEmail);
			return true;
		} catch (EmptyResultDataAccessException ex) {
			return false;
		} catch (DataAccessException ex) {
			throw new RuntimeException("Error fetching user by username: " + userEmail, ex);

		}
	}

}
