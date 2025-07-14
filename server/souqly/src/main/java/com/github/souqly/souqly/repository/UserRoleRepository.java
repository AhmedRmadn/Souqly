package com.github.souqly.souqly.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.souqly.souqly.model.UserRole;
import com.github.souqly.souqly.repository.rowmapper.UserRoleRowMapper;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class UserRoleRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRoleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void assignRoleToUser(String userId, String roleId) {
        String sql = """
            INSERT INTO user_role (user_id, role_id, created_at, updated_at)
            VALUES (?, ?, ?, ?)
        """;

        LocalDateTime now = LocalDateTime.now();
        jdbcTemplate.update(sql, userId, roleId, now, now);
    }

    public List<UserRole> findRolesByUserId(String userId) {
        String sql = "SELECT * FROM user_role WHERE user_id = ?";
        return jdbcTemplate.query(sql, new UserRoleRowMapper(), userId);
    }

    public boolean exists(String userId, String roleId) {
        String sql = "SELECT COUNT(*) FROM user_role WHERE user_id = ? AND role_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, roleId);
        return count != null && count > 0;
    }

    public void deleteRoleFromUser(String userId, String roleId) {
        String sql = "DELETE FROM user_role WHERE user_id = ? AND role_id = ?";
        jdbcTemplate.update(sql, userId, roleId);
    }
}