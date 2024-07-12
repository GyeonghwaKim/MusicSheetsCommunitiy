package com.example.youtubeSheet.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class YoutubeValidator implements ConstraintValidator<Youtube,String> {

    private static final String YOUTUBE_URL_REGEX =
            "^(https?://)?(www\\.)?(youtube\\.com/watch\\?v=|youtu\\.be/)[A-Za-z0-9_-]{11}([&?].*)?$";


    private static final Pattern YOUTUBE_URL_PATTERN = Pattern.compile(YOUTUBE_URL_REGEX);

    @Override
    public void initialize(Youtube constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        return YOUTUBE_URL_PATTERN.matcher(value).matches();
    }
}
