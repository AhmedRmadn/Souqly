package com.github.souqly.souqly.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateAddressRequest {


    @NotBlank(message = "Building name is required")
    @Size(max = 255, message = "Building name must not exceed 255 characters")
    private String buildingName;

    @NotBlank(message = "Street is required")
    @Size(max = 255, message = "Street must not exceed 255 characters")
    private String street;

    @NotBlank(message = "City is required")
    @Size(max = 255, message = "City must not exceed 255 characters")
    private String city;

    @NotBlank(message = "State is required")
    @Size(max = 255, message = "State must not exceed 255 characters")
    private String state;

    @NotBlank(message = "Country is required")
    @Size(max = 255, message = "Country must not exceed 255 characters")
    private String country;

    @NotBlank(message = "Pincode is required")
    @Size(max = 20, message = "Pincode must not exceed 20 characters")
    private String pincode;

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
}
