package com.github.souqly.souqly.repository.rowmapper;
import com.github.souqly.souqly.model.Address;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AddressRowMapper implements RowMapper<Address> {
    @Override
    public Address mapRow(ResultSet rs, int rowNum) throws SQLException {
        Address address = new Address();
        address.setAddressId(rs.getString("address_id"));
        address.setUserId(rs.getString("user_id"));
        address.setBuildingName(rs.getString("building_name"));
        address.setStreet(rs.getString("street"));
        address.setCity(rs.getString("city"));
        address.setState(rs.getString("state"));
        address.setCountry(rs.getString("country"));
        address.setPincode(rs.getString("pincode"));
        address.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        address.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return address;
    }
}

