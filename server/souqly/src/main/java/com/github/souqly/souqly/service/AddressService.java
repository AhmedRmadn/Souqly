package com.github.souqly.souqly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.souqly.souqly.Exception.UnAuthorizedException;
import com.github.souqly.souqly.model.Address;
import com.github.souqly.souqly.payload.request.CreateAddressRequest;
import com.github.souqly.souqly.payload.request.UpdateAddressRequest;
import com.github.souqly.souqly.payload.response.AddressResponse;
import com.github.souqly.souqly.payload.response.AllRecordsResponse;
import com.github.souqly.souqly.repository.AddressRepository;
 import java.util.*;
@Service
public class AddressService {

	@Autowired
	AddressRepository addressRepository;

	@Autowired
	Mapper mapper;

	public AddressResponse addUserAddress(CreateAddressRequest createAddressRequest, String userId) {
		Address address = new Address();

		// Set fields from request
		address.setUserId(userId);
		address.setBuildingName(createAddressRequest.getBuildingName());
		address.setStreet(createAddressRequest.getStreet());
		address.setCity(createAddressRequest.getCity());
		address.setState(createAddressRequest.getState());
		address.setCountry(createAddressRequest.getCountry());
		address.setPincode(createAddressRequest.getPincode());

		// Save to DB
		Address savedAddress = addressRepository.save(address);

		// Return response
		return mapper.mapAddressToResoponse(savedAddress);
	}

	public AddressResponse findAddressById(String addressId, String userId) {
		Address address = addressRepository.findById(addressId);

		if (!address.getUserId().equals(userId)) {
			throw new UnAuthorizedException("You are not authorized to access this address.");
		}

		return mapper.mapAddressToResoponse(address);
	}

	public void deleteAddress(String addressId, String userId) {
		Address address = addressRepository.findById(addressId);

		if (!address.getUserId().equals(userId)) {
			throw new UnAuthorizedException("You are not authorized to delete this address.");
		}

		addressRepository.deleteById(addressId);
	}

	@Transactional
	public AddressResponse updateUserAddress(UpdateAddressRequest updateAddressRequest,String addressId, String userId) {
		// Step 1: Fetch existing address
		Address address = addressRepository.findById(addressId);

		// Step 2: Verify ownership
		if (!address.getUserId().equals(userId)) {
			throw new UnAuthorizedException("You are not authorized to update this address.");
		}

		// Step 3: Update fields
		address.setBuildingName(updateAddressRequest.getBuildingName());
		address.setStreet(updateAddressRequest.getStreet());
		address.setCity(updateAddressRequest.getCity());
		address.setState(updateAddressRequest.getState());
		address.setCountry(updateAddressRequest.getCountry());
		address.setPincode(updateAddressRequest.getPincode());

		// Step 4: Let repository handle updated_at internally and persist
		address = addressRepository.update(address);

		// Step 5: Convert and return response
		return mapper.mapAddressToResoponse(address);
	}
	
	public AllRecordsResponse<AddressResponse> getUserAddresses(String userId) {
	    // Step 1: Fetch addresses from the repository
	    List<Address> addresses = addressRepository.findAllByUserId(userId);

	    // Step 2: Convert entities to response DTOs
	    List<AddressResponse> addressResponses = addresses.stream()
	            .map(mapper::mapAddressToResoponse)
	            .toList();

	    // Step 3: Wrap the result in AllRecordsResponse
	    AllRecordsResponse<AddressResponse> response = new AllRecordsResponse<>();
	    response.setContent(addressResponses);
	    response.setTotalElements(addressResponses.size());

	    return response;
	}
	


}
