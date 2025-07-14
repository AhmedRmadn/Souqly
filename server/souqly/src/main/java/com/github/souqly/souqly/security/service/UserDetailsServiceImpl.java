package com.github.souqly.souqly.security.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.souqly.souqly.model.User;
import com.github.souqly.souqly.model.UserRole;
import com.github.souqly.souqly.repository.RoleRepository;
import com.github.souqly.souqly.repository.UserRepository;
import com.github.souqly.souqly.repository.UserRoleRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	UserRepository userRepository;

	@Autowired
	UserRoleRepository userRoleRepository;

	@Autowired
	RoleRepository roleRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUserName(username)
			    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
		
		List<UserRole> userRoles = userRoleRepository.findRolesByUserId(user.getUserId());
		Set<String> roles = new HashSet<String>();
		for (UserRole userRole : userRoles) {
			roles.add(roleRepository.findById(userRole.getRoleId()).getRoleName().name());
		}
		return UserDetailsImpl.build(user, roles);
	}

}