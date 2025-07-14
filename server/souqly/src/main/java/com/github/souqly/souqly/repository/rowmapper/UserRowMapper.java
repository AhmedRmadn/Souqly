package com.github.souqly.souqly.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.github.souqly.souqly.model.User;

public class UserRowMapper implements RowMapper<User>{

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setUserId(rs.getString("user_id"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setUserName(rs.getString("user_name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return user;
    }
}
