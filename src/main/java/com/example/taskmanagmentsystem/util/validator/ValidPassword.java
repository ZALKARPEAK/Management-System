package com.example.taskmanagmentsystem.util.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface ValidPassword {
    String message() default "Неверный формат пароля";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}