package com.github.souqly.souqly.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RoleSubsetValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRoles {
    String message() default "Invalid role(s) provided";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
