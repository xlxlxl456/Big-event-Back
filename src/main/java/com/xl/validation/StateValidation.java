package com.xl.validation;

import com.xl.anno.State;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StateValidation implements ConstraintValidator<State,String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value==null){
            return false;
        }

        if(value.equals("Released") || value.equals("Draft")){
            return true;
        }

        return false;
    }
}
