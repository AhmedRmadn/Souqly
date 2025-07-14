package com.github.souqly.souqly.model;

import java.time.LocalDateTime;

public class Role {
    private String roleId;
    private RoleName roleName; // enum
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public RoleName getRoleName() {
		return roleName;
	}
	public void setRoleName(RoleName roleName) {
		this.roleName = roleName;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
    
    

}
