package com.springreactive.webfluxcourse.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = {TrimStringValidator.class})
@Target(FIELD)
@Retention(RUNTIME)
public @interface TrimString {

    String message() default "Field cannot have blank spaces";

    /**
     * Classes que representam grupos de validação
     *
     * @return
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
