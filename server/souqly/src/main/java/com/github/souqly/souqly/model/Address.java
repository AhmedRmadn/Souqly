package com.github.souqly.souqly.model;

import java.time.LocalDateTime;

public class Address {
    private String addressId;
    private String userId;
    private String buildingName;
    private String street;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public Address() {}

    public Address(String addressId, String userId, String buildingName, String street, String city,
                   String state, String country, String pincode,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.addressId = addressId;
        this.userId = userId;
        this.buildingName = buildingName;
        this.street = street;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }



    public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
