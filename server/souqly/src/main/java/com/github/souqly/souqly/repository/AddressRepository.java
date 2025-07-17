package com.github.souqly.souqly.repository;

import com.github.souqly.souqly.Exception.DeleteDatabaseException;
import com.github.souqly.souqly.Exception.FetchDatabaseException;
import com.github.souqly.souqly.Exception.InsertDatabaseException;
import com.github.souqly.souqly.Exception.UpdateDatabaseException;
import com.github.souqly.souqly.model.Address;
import com.github.souqly.souqly.repository.rowmapper.AddressRowMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AddressRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String generateUUIDForAddress() {
		return UUID.randomUUID().toString();
	}

	public Address save(Address address) {
		String sql = """
				INSERT INTO addresses (
				    address_id, user_id, building_name, street,
				    city, state, country, pincode, created_at, updated_at
				) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
				""";

		String addressId = generateUUIDForAddress();
		LocalDateTime now = LocalDateTime.now();

		address.setAddressId(addressId);
		address.setCreatedAt(now);
		address.setUpdatedAt(now);

		int row = jdbcTemplate.update(sql, address.getAddressId(), address.getUserId(), address.getBuildingName(),
				address.getStreet(), address.getCity(), address.getState(), address.getCountry(), address.getPincode(),
				Timestamp.valueOf(address.getCreatedAt()), Timestamp.valueOf(address.getUpdatedAt()));
		if (row != 1)
			throw new InsertDatabaseException("error while inserting into address ");

		return address;
	}

	public Address findById(String addressId) {
		String sql = "SELECT * FROM addresses WHERE address_id = ?";
		try {
			Address address = jdbcTemplate.queryForObject(sql, new AddressRowMapper(), addressId);
			return address;
		} catch (DataAccessException e) {
			throw new FetchDatabaseException("error while fetching  address with id " + addressId);
		}

	}

	public List<Address> findAllByUserId(String userId) {
		String sql = "SELECT * FROM addresses WHERE user_id = ?";
		return jdbcTemplate.query(sql, new AddressRowMapper(), userId);
	}

	public Address update(Address address) {
		String sql = """
				UPDATE addresses SET
				    building_name = ?, street = ?, city = ?, state = ?,
				    country = ?, pincode = ?, updated_at = ?
				WHERE address_id = ?
				""";

		LocalDateTime now = LocalDateTime.now();
		address.setUpdatedAt(now);

		int row = jdbcTemplate.update(sql, address.getBuildingName(), address.getStreet(), address.getCity(),
				address.getState(), address.getCountry(), address.getPincode(),
				Timestamp.valueOf(address.getUpdatedAt()), address.getAddressId());
		if (row != 1)
			throw new UpdateDatabaseException("could not update Address with id " + address.getAddressId());
		return address;
	}

	public void deleteById(String addressId) {
		String sql = "DELETE FROM addresses WHERE address_id = ?";
		int row = jdbcTemplate.update(sql, addressId);
		if (row != 1)
			throw new DeleteDatabaseException("could not delete Address with id " + addressId);
	}

}
