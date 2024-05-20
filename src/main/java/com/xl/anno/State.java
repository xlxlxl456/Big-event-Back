package com.xl.anno;

import com.xl.validation.StateValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(
        validatedBy = {StateValidation.class}
)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface State {
    String message() default "{state must be Released or Draft}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
