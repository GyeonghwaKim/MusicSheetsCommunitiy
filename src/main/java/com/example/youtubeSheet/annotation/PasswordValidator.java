package com.example.youtubeSheet.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<Password,String> {

    private static final String PASSWORD_REGEX ="^(?=.*[a-z])(?=.*[0-9])[a-z0-9]{8,20}$";

    private static final Pattern PASSWORD_URL_PATTERN=Pattern.compile(PASSWORD_REGEX);

    @Override
    public void initialize(Password constraintAnnotation) {

    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if(password==null || password.isEmpty()){
            return false;
        }
        return PASSWORD_URL_PATTERN.matcher(password).matches();
    }
}
