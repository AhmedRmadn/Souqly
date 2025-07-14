package com.github.souqly.souqly.repository.rowmapper;

import org.springframework.jdbc.core.RowMapper;

import com.github.souqly.souqly.model.UserRole;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRoleRowMapper implements RowMapper<UserRole> {
    @Override
    public UserRole mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserRole userRole = new UserRole();
        userRole.setUserId(rs.getString("user_id"));
        userRole.setRoleId(rs.getString("role_id"));
        userRole.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        userRole.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return userRole;
    }
}
