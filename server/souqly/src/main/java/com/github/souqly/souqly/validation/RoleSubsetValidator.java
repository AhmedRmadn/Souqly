package com.github.souqly.souqly.validation;

import com.github.souqly.souqly.model.RoleName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class RoleSubsetValidator implements ConstraintValidator<ValidRoles, Set<String>> {

    @Override
    public boolean isValid(Set<String> value, ConstraintValidatorContext context) {
        if (value == null) return true; // null is handled elsewhere

        for (String role : value) {
            try {
                RoleName.valueOf(role.toUpperCase()); // case-insensitive match
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return true;
    }
}
