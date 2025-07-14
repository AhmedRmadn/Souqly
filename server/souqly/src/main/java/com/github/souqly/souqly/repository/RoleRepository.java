package com.github.souqly.souqly.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.souqly.souqly.Exception.FetchDatabaseException;
import com.github.souqly.souqly.model.Role;
import com.github.souqly.souqly.model.RoleName;
import com.github.souqly.souqly.repository.rowmapper.RoleRowMapper;

@Repository
public class RoleRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String generateUUIDForRole() {
		return UUID.randomUUID().toString();
	}

	public Role createRole(RoleName roleName) {
		String insertQuery = "INSERT INTO roles (role_id, role_name, created_at, updated_at) VALUES (?, ?, ?, ?)";

		Role role = new Role();
		role.setRoleId(generateUUIDForRole());
		role.setRoleName(roleName);
		role.setCreatedAt(LocalDateTime.now());
		role.setUpdatedAt(LocalDateTime.now());

		try {
			int rowsAffected = jdbcTemplate.update(insertQuery, role.getRoleId(), role.getRoleName().name(),
					role.getCreatedAt(), role.getUpdatedAt());

			if (rowsAffected > 0) {
				return role;
			} else {
				throw new RuntimeException("Failed to insert role into the database.");
			}
		} catch (DataAccessException ex) {
			throw new RuntimeException("Database error while inserting role: " + ex.getMessage(), ex);
		}
	}

	public Role findById(String roleId) {
		String query = "SELECT * FROM roles WHERE role_id = ?";
		try {
			Role role = jdbcTemplate.queryForObject(query, new RoleRowMapper(), roleId);
			return role;
		} catch (EmptyResultDataAccessException ex) {
			throw new FetchDatabaseException("Database error while fetching role with ID: " + roleId);
		} catch (DataAccessException ex) {
			throw new FetchDatabaseException("Database error while fetching role with ID: " + roleId);
		}
	}

	public Optional<Role> findByName(RoleName roleName) {
		String query = "SELECT * FROM roles WHERE role_name = ?";
		try {
			Role role = jdbcTemplate.queryForObject(query, new RoleRowMapper(), roleName.name());
			return Optional.of(role);
		} catch (EmptyResultDataAccessException ex) {
			return Optional.empty();
		} catch (DataAccessException ex) {
			throw new RuntimeException("Database error while fetching role with name: " + roleName, ex);
		}
	}

}
